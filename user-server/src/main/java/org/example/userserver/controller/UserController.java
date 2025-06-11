package org.example.userserver.controller;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.vo.response.AccountInfoVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.userserver.service.UserDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDataService userDataService;

    @GetMapping("/userInfo")
    public RestBean<AccountInfoVO> getUserInfo(@RequestParam("username") @Email String username) {
        AccountInfoVO accountInfoVO = userDataService.getUserAccountInfo(username);
        return accountInfoVO == null
                ? RestBean.failure(400, "用户信息获取失败")
                : RestBean.success(accountInfoVO);
    }

    @ResponseBody
    @GetMapping("/getUserTableList")
    public RestBean<TableListVO> readTableContent(@RequestParam int page, @RequestParam int pageSize) {
        String message= userDataService.getUserTableListVerify(page,pageSize);
        return (message!=null) ?
                RestBean.failure(400,message): RestBean.success(userDataService.getUserTableList(page,pageSize));
    }
    //搜索
    @ResponseBody
    @GetMapping("/searchUserTableList")
    public <T> RestBean<TableListVO> searchTableList(@RequestParam T searchText,@RequestParam int page,@RequestParam int pageSize) {
        String message=userDataService.getUserTableListVerify(page,pageSize);
        return (message!=null)?
                RestBean.failure(400,message) : RestBean.success(userDataService.getSearchDataTableList(searchText,page,pageSize));
    }
    //用户编辑查询
    @ResponseBody
    @GetMapping("/getPersonalOne")
    public RestBean<TableListVO> getPersonalOne(@RequestParam @Email String username) {
        String message=userDataService.getUserTableListVerify(username);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success(userDataService.getUserTableList(username));
    }
    //用户编辑修改
    @ResponseBody
    @GetMapping("/changePersonalOne")
    public RestBean<Void> changePersonalOne(@RequestParam @Email String username,
                                            @RequestParam String name,
                                            @RequestParam String sex,
                                            @RequestParam(required = false) String birthDateString,
                                            @RequestParam String region,
                                            @RequestParam String signature) {
//        System.out.println(signature);
        String message=userDataService.changeUsersData(username,name,sex,birthDateString,region,signature);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //用户删除
    @ResponseBody
    @GetMapping("/deleteUsers")
    public RestBean<Void> deleteUsers(@RequestParam List<Long> userIds){
        String message=userDataService.deleteUserTableList(userIds);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //提交用户头像图片
    @ResponseBody
    @PostMapping("/upUserAvatar")
    public RestBean<Void> upUserAvatar(@RequestParam int user_id,@RequestParam MultipartFile file){
        String message=userDataService.uploadFile(user_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //获取用户图片
    @GetMapping("/getUserAvatar")
    public ResponseEntity<org.springframework.core.io.Resource> getUserAvatar(
            @RequestParam("user_id") int user_id) {
        return userDataService.getFile(user_id);
    }

    //----个人页数据查询
    @ResponseBody
    @GetMapping("/getRootPersonalOne")
    public RestBean<TableListVO> getRootPersonalOne(@RequestParam @Email String username) {
        String message=userDataService.getRootUserTableListVerify(username);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success(userDataService.getRootUserTableList(username));
    }


}
