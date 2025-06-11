package org.example.songserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.songserver.service.SongDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {

    private final SongDataService songDataService;

    @GetMapping("/getSongSum")
    public Integer getSongSum() {
        return songDataService.getSongSum();
    }
    @GetMapping("/getSongTypeList")
    public HomeDataList[] getSongTypeList() {
        return songDataService.getSongTypeList();
    }

}
