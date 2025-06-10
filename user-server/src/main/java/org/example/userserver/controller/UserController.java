package org.example.userserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.vo.response.HoneDataSumVO;
import org.example.userserver.service.UserDataService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDataService userDataService;

    @GetMapping("/getUserAvatar")
    public ResponseEntity<Resource> getUserAvatar(@RequestParam int user_id){
        return userDataService.getFile(user_id);
    }


}
