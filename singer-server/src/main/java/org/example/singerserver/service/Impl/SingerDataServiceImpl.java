package org.example.singerserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.dto.SingerDataList;
import org.example.commoncore.entity.vo.request.SingerAddVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.singerserver.mapper.SingerDataMapper;
import org.example.singerserver.service.SingerDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.example.singerserver.utils.GetAvatar.getResourceResponseEntity;

@Service
public class SingerDataServiceImpl extends ServiceImpl<SingerDataMapper, SingerDataList> implements SingerDataService {

    @Resource
    private SingerDataMapper indexSingerDataMapper;

    @Override
    public TableListVO getSingerTableList(int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page =(page-1)*pageSize;
        SingerDataList[] singerDataList = indexSingerDataMapper.getSingerDataList(page,pageSize);
        int count=indexSingerDataMapper.getSingerSum();
        TableListVO vo = new TableListVO();
        vo.setCount(count);
        vo.setSingerDataList(singerDataList);
        return vo;
    }

    @Override
    public <T> TableListVO getSearchDataTableList(T searchText, int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page=(page-1)*pageSize;
        String keyName=String.valueOf(searchText);
        SingerDataList[] singerDataLists=indexSingerDataMapper.getSingerDataByKeyName(keyName,page,pageSize);
        int totalCount=indexSingerDataMapper.getSearchSingerCount(keyName);
        TableListVO vo = new TableListVO();
        vo.setCount(totalCount);
        vo.setSingerDataList(singerDataLists);
        return vo;
    }

    //----删除
    @Override
    public String deleteSingerTables(List<Long> singerIds) {
//        System.out.println(singerIds);
        indexSingerDataMapper.deleteSingers(singerIds);
        return null;
    }
    //----添加
    @Override
    public String addSingerTable(SingerAddVO vo) {
        if (vo.getSinger_name()==null || vo.getSinger_name().isEmpty()) {
            return "歌手名为空，请输入歌手名";
        }
        if (vo.getBirth_date()==null || vo.getBirth_date().isEmpty()) vo.setBirth_date(null);
        if (vo.getBirth_date()!=null) {
            try {
                Date.valueOf(vo.getBirth_date());// 将字符串转换为java.sql.Date
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "请求的生日格式有误";
            }
        }
        try{
            indexSingerDataMapper.insertSinger(vo);
        }catch (Exception e){
            return "不能重复歌手";
        }
        return null;
    }
    //----修改
    @Override
    public String updateSingerTable(int singer_id, String singer_name, String sex, String nationality, String birthDateString, String intro) {
        Date birth_date=null;
        SingerDataList singerDataOne=indexSingerDataMapper.getSingerDataBySingerId(singer_id);
        if (singerDataOne==null) return "暂无此歌手";
        if (singerDataOne.getSinger_name()!=null && (singer_name==null || singer_name.isEmpty())) singer_name=null;
        if (singer_name==null || singer_name.isEmpty()) singer_name=singerDataOne.getSinger_name();
        if (sex==null || sex.isEmpty()) sex=singerDataOne.getSex();
        if (nationality==null || nationality.isEmpty()) nationality=singerDataOne.getNationality();
        if (birthDateString==null || birthDateString.isEmpty()) birthDateString=null;
        if (intro==null || intro.isEmpty()) intro=null;

//        if (birthDateString != null) {
//            try {
//                birth_date = Date.valueOf(birthDateString); // 将字符串转换为java.sql.Date
//            } catch (IllegalArgumentException ex) {
//                System.out.println(ex.getMessage());
//                return "请求的生日格式有误";
//            }
//        }
        indexSingerDataMapper.updateSinger(singer_id,singer_name,sex,nationality,birth_date,intro);
        return null;
    }

    //判断是否存在

    //查询单个歌手信息
    @Override
    public TableListVO getSingerDataList(int singer_id) {
        SingerDataList singerDataList=indexSingerDataMapper.getSingerDataBySingerId(singer_id);
        TableListVO vo=new TableListVO();
        vo.setSingerDataOne(singerDataList);
        return vo;
    }

    //提交头像照片
    @Override
    public String uploadFile(int singer_id,MultipartFile file) {
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
        if (indexSingerDataMapper.getSingerDataBySingerId(singer_id)==null) return "不存在该歌手";
        // 创建新文件名对象
        File dest = new File(fullFileName);
        //保存文件
        try {
            file.transferTo(dest);
            indexSingerDataMapper.updateSingerPath(singer_id,fullFileName);
            return null;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace(); // 可以打印错误日志帮助调试
            return "保存失败";
        }
    }
    //获取歌手图片
    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getFile(int singer_id) {
        return getResourceResponseEntity(indexSingerDataMapper.getSingerPath(singer_id));
    }


    //----工具方法
    @Override
    public String getSingerTableListVerify(int page, int pageSize) {
        return (page<1 || pageSize<1) ? "请求参数（page,pageSize）不能为小于1的数":null;
    }


}
