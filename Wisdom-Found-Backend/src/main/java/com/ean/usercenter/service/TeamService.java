package com.ean.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ean.usercenter.model.domain.Team;
import com.ean.usercenter.model.domain.User;
import com.ean.usercenter.model.dto.TeamQuery;
import com.ean.usercenter.model.request.TeamJoinRequest;
import com.ean.usercenter.model.request.TeamQuitRequest;
import com.ean.usercenter.model.request.TeamUpdateRequest;
import com.ean.usercenter.model.vo.TeamUserVO;

import java.util.List;


/**
* @author Asphyxia
* @description 针对表【team】的数据库操作Service
* @createDate 2023-02-12 16:20:30
*/
public interface TeamService extends IService<Team> {
    // 传递一个队伍和创建队伍的用户
    long addTeam(Team team, User loginUser);

    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);

    boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser);

    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    boolean deleteTeam(Long id, User loginUser);
}
