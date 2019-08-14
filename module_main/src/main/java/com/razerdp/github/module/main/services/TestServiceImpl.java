package com.razerdp.github.module.main.services;

import com.razerdp.github.lib.utils.UIHelper;
import com.razerdp.github.router.TestService;
import com.razerdp.lib.annotations.ServiceImpl;

/**
 * Created by 大灯泡 on 2019/8/14.
 */
@ServiceImpl
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        UIHelper.toast("Test");
    }
}
