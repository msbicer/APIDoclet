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
 * @module Movie
 * @author sbicer
 *
 */
@Controller
@RequestMapping(value={"/movie","/movie2"})
public class MovieController {
	/**
	 * @name GetMovie
	 * @param name
	 *            Name of movie
	 * @param query
	 *            Query String
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "{name}/{param}", method = RequestMethod.GET)
	public Movie getMovie(@PathVariable Movie name,
			@PathVariable("param") Boolean testParam,
			@RequestParam(required = true, value="test4") String testParam2,
			@RequestParam(required = true, defaultValue="test") String query, ModelMap model) {

		model.addAttribute("movie", name);
		
		Movie val = new Movie();
		val.setName("list");
		
		return val;

	}
	/**
	 * Lists movies
	 * @return Test value
	 * @requestExample http://example.com/info/id/1234
	 * @responseExample 
{
  result: {
    id: 1737
    imagePath: http://example.com/images/[size]/Rm/Vy/RmVyZGkgw5Z6YmXEn2Vu.jpg
    name: "Info name"
  }
}
	 */
	@RequestMapping(value={"/list1", "/list2"})
	public @ResponseBody String listMovies(){
		return null;
	}
	/**
	 * @name get
	 * @param id
	 * @return
	 */
	@RequestMapping(value="get")
	public ModelAndView getModelAndView(@RequestParam Long id){
		return null;
	}
	
	@RequestMapping(value="post")
	public void post(){
	}
	
	/**
	 * @ignore
	 */
	@RequestMapping(value="dummy")
	public void dummy(){
		
	}

}