package clustered.data.warehouse.controllers.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class DataWarehouseControllerAdvice {

	@ExceptionHandler(value = Exception.class)
	public ModelAndView errorHandler(HttpServletRequest req, Exception e) throws Exception {

		ModelAndView mav = new ModelAndView();
		mav.addObject("message", e.getMessage());
		mav.setViewName("error");
		return mav;
	}
}
