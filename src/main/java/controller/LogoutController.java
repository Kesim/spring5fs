package controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); //세션을 제거하여 로그아웃
		return "redirect:/main";
	}
	
}
