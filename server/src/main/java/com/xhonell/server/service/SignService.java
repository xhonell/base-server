package com.xhonell.server.service;

import java.util.List;

/**
 * program: BaseServer
 * ClassName SignService
 * description:
 * author: xhonell
 * create: 2025年10月19日22时20分
 * Version 1.0
 **/
public interface SignService {
    void signToday();

    List<Integer> signMonth();
}
