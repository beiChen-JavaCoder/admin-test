package com.admin.controller.player;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PlayerVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.domain.vo.RevenueVo;
import com.admin.service.PlayerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xqf
 * @version 1.0
 */
@Api("玩家管理")
@RestController()
@RequestMapping("/player")
@PreAuthorize("@ss.hasRole('merchant')")
public class PlayerController {


    @Autowired
    private PlayerService playerService;

    @PostMapping("/list")
    public ResponseResult getList(@RequestBody PlayerVo queryParamsVo) {
        return playerService.findPlayerPage(queryParamsVo);
    }

    @PostMapping("/flow")
    public ResponseResult getFlow(@RequestBody QueryParamsVo queryParamsVo) {
        return playerService.findFlowPage(queryParamsVo);
     }

    @PostMapping("/revenue")
    public ResponseResult getRevenue(@RequestBody RevenueVo revenueVo) {
        return playerService.findRevenuePage(revenueVo);
    }
}
