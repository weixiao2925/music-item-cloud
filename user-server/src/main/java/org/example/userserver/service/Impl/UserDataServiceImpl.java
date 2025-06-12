package org.example.userserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.UserDataList;
import org.example.commoncore.entity.vo.response.AccountInfoVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.userserver.mapper.IndexUserDataMapper;
import org.example.userserver.service.UserDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.example.userserver.utils.GetAvatar.getResourceResponseEntity;

@Service
@RequiredArgsConstructor
public class UserDataServiceImpl extends ServiceImpl<IndexUserDataMapper, UserDataList> implements UserDataService {

    @Resource
    private IndexUserDataMapper indexUserDataMapper;

    @Override
    public Integer getUserSum() {
        return indexUserDataMapper.getUserSum();
    }
    @Override
    public HomeDataList[] getUserSexList() {
        return indexUserDataMapper.getUserSexList();
    }

    @Override
    public AccountInfoVO getUserAccountInfo(String username) {
        return indexUserDataMapper.getUserAccountInfo(username);
    }

    //获取用户信息
    public TableListVO getUserTableList(int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page=(page-1)*pageSize;
        List<UserDataList> userDataList = indexUserDataMapper.getUserDataList(page, pageSize);
        TableListVO tableListVO = new TableListVO();
        tableListVO.setUserDataList(userDataList.toArray(new UserDataList[0]));

        int totalCount = indexUserDataMapper.getUserCount(); // 获取总条目数
//        int totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
        tableListVO.setCount(totalCount);
        return tableListVO;
    }


    //----搜索
    public <T> TableListVO getSearchDataTableList(T searchText, int page, int pageSize){
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page=(page-1)*pageSize;
        String keyName=String.valueOf(searchText);
        UserDataList[] userDataLists=indexUserDataMapper.getUserDataByKeyName(keyName,page,pageSize);
        int totalCount = indexUserDataMapper.getSearchUserCount(keyName); // 获取总条目数
        TableListVO vo=new TableListVO();
        vo.setCount(totalCount);
        vo.setUserDataList(userDataLists);
        return vo;
    }

    @Override
    public TableListVO getRootUserTableList(String username) {
        UserDataList userDataOne=indexUserDataMapper.getRootUserData(username);
        if(userDataOne==null) throw new IllegalArgumentException("请求账号出错");
        TableListVO vo=new TableListVO();
        vo.setUserDataOne(userDataOne);
        return vo;
    }

    @Override
    public TableListVO getUserTableList(String username) {
        UserDataList userDataOne=indexUserDataMapper.getUserData(username);
//        System.out.println(username);
        if(userDataOne==null) throw new IllegalArgumentException("请求账号出错");
        TableListVO vo=new TableListVO();
        vo.setUserDataOne(userDataOne);
        return vo;
    }

    //编辑用户信息
    @Override
    public String changeUsersData(String username, String name, String sex, String birthDateString, String region,String signature) {
        Date birth_date=null;
        UserDataList userDataOne=indexUserDataMapper.getUserData(username);
        if(userDataOne==null) {
            userDataOne=indexUserDataMapper.getRootUserData(username);
            if(userDataOne==null) return "请求账号出错";
        }
        if (name==null || name.isEmpty()) name=userDataOne.getName();
        if (sex==null || sex.isEmpty()) sex=userDataOne.getSex();
        if (birthDateString==null || birthDateString.isEmpty()) birth_date=userDataOne.getBirth_date();
        if (region==null || region.isEmpty()) region=userDataOne.getRegion();
        if (signature==null || signature.isEmpty()) signature=userDataOne.getSignature();

        if (birthDateString != null && !birthDateString.isEmpty()) {
            try {
                birth_date = Date.valueOf(birthDateString); // 将字符串转换为java.sql.Date
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "请求的生日格式有误";
            }
        }
//        System.out.println(birth_date);
        indexUserDataMapper.changeUsersData(username,name,sex,birth_date,region,signature);
        return null;
    }

    //删除用户信息(未完善)
    @Override
    public String deleteUserTableList(List<Long> userIds) {
//        System.out.println(userIds);
        indexUserDataMapper.deleteUsers(userIds);
        return null;
    }

    @Override
    public String uploadFile(int user_id, MultipartFile file) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
//        System.out.println("原始文件名"+fileName);
        // 允许的文件后缀名集合
        List<String> allowedSuffixes = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); // 转换为小写以做不区分大小写的比较
//        System.out.println("后缀"+suffix);
        if (!allowedSuffixes.contains(suffix)) {
            return "请上传图片文件";
        }
        fileName= UUID.randomUUID()+suffix;
        //完整地址
        String fullFileName = Const.AVATAR_PATH+fileName;
        if (indexUserDataMapper.isExist(user_id)==null) return "不存在该用户";
        // 创建新文件名对象
        File dest = new File(fullFileName);
        //保存文件
        try {
            file.transferTo(dest);
            indexUserDataMapper.updateSingerPath(user_id,fullFileName);
            return null;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace(); // 可以打印错误日志帮助调试
            return "保存失败";
        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getFile(int user_id) {
//        System.out.println("user_id: " + user_id);
        return getResourceResponseEntity(indexUserDataMapper.getUserPath(user_id));
    }

    @Override
    public void deleteSingerSongRelation(List<Long> songIds) {
        indexUserDataMapper.deleteUserSongRelation(songIds);
    }

    //----工具方法
    //验证页数是否合法
    public String getUserTableListVerify(int page, int pageSize){
        return  (page<1 || pageSize<1) ?"请求参数（page,pageSize）不能为小于1的数" : null;
    }
    //验证用户是否存在，并验证是否为管理员
    public String getRootUserTableListVerify(String username){
        UserDataList userDataOne=indexUserDataMapper.getRootUserData(username);
        if(userDataOne==null) return "请求账号出错";
        return null;
    }
    //验证用户是否存在，并验证是否为管理员
    public String getUserTableListVerify(String username){
        UserDataList userDataOne=indexUserDataMapper.getUserData(username);
        if(userDataOne==null) return "请求账号出错";
        return null;
    }

}
