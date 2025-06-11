package org.example.userserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.commoncore.entity.dto.UserDataList;
import org.example.commoncore.entity.vo.response.AccountInfoVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserDataService extends IService<UserDataList> {
    AccountInfoVO getUserAccountInfo(String username);

    TableListVO getUserTableList(int page, int pageSize) ;
    String getUserTableListVerify(int page, int pageSize);
    <T> TableListVO getSearchDataTableList(T searchText, int page, int pageSize);

    TableListVO getRootUserTableList(String username);
    String getRootUserTableListVerify(String username);

    TableListVO getUserTableList(String username);
    String getUserTableListVerify(String username);

    String changeUsersData(String username, String name, String sex, String birthDateString, String region,String signature);

    String deleteUserTableList(List<Long> userIds);

    String uploadFile(int user_id, MultipartFile file);
    ResponseEntity<Resource> getFile(int user_id);
}
