package controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.DuplicateMemberException;
import spring.MemberRegisterService;
import spring.RegisterRequest;

@Controller
public class RegisterController {
	
	private MemberRegisterService memberRegisterService;
	
	public void setMemberRegisterService(MemberRegisterService memberRegisterService) {
		this.memberRegisterService = memberRegisterService;
	}

	@RequestMapping("/register/step1")
	public String handleStep1() {
		return "register/step1";
	}
	
	@PostMapping("/register/step2") //get방식으로 주소창에 직접 주소를 입력하면 접속이 되지않음. post방식으로만 가능
	public String handleStep2(
			@RequestParam(value = "agree", defaultValue = "false") Boolean agree,
			Model model) {
		if(!agree)
			return "register/step1";
		
		model.addAttribute("registerRequest", new RegisterRequest()); //커맨드 객체 전달. 스프링 폼 태그에서 이 객체를 사용
		return "register/step2";
	}
	
	@GetMapping("/register/step2") //주소창에 직접 주소를 입력하면 에러창 대신 step1으로 리다이렉트 처리
	public String handleStep2Get() {
		return "redirect:/register/step1";
	}
	
	@PostMapping("/register/step3")
	public String handleStep3(@Valid RegisterRequest regReq, Errors errors) { //파라미터로 커맨드객체를 사용, 커맨드 객체 검증을 위한 Errors 객체
		//커맨드 객체 파라미터에 @Valid를 사용하면 이 메서드 시작 전에 먼저 검증하여 결과를 errors에 저장한다.
		//따라서 기존의 밑의 코드처럼 직접 검증을 시작하는 코드는 필요 없다.
		//new RegisterRequestValidator().validate(regReq, errors); //현재 메서드의 파라미터인 errors를 인자로 사용
		if(errors.hasErrors()) //입력한 값 자체에 문제가 있는지 검증(db사용 안함)
			return "register/step2";
		try {
			memberRegisterService.regist(regReq);
			return "register/step3";
		} catch(DuplicateMemberException ex) {
			errors.rejectValue("email", "duplicate"); //db와 연결하여 에러 검증
			return "register/step2";
		}
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) { //현재 컨트롤러에서 사용할 validator 지정.
		binder.setValidator(new RegisterRequestValidator());
	}
	
}
