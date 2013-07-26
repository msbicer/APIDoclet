package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.model.Movie;
/**
 * @ignore
 *
 */
@Controller
@RequestMapping(value={"/test"})
public class IgnoreController {
	@RequestMapping(value="get")
	public @ResponseBody String get(){
		return "dummy";
	}
}