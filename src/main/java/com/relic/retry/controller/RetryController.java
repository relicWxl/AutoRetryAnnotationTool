//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.controller;

import com.relic.retry.pojo.dto.PageResult;
import com.relic.retry.pojo.dto.RetryJobQuery;
import com.relic.retry.pojo.vo.RetryJobVO;
import com.relic.retry.service.RetryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RetryController {
    private final RetryService retryService;

    @GetMapping({"/retry"})
    public ModelAndView getRetryView() {
        return new ModelAndView("forward:/static/retry.html");
    }

    @ResponseBody
    @GetMapping(
            value = {"/api/v1/retry/actions/query"},
            produces = {"application/json"}
    )
    public PageResult<RetryJobVO> pageQuery(RetryJobQuery retryJobQuery) {
        return this.retryService.pageQuery(retryJobQuery);
    }

    public RetryController(RetryService retryService) {
        this.retryService = retryService;
    }
}
