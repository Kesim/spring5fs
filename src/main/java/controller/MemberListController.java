package controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.Member;
import spring.MemberDao;

@Controller
public class MemberListController {

	private MemberDao memberDao;

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@RequestMapping("/members")
	public String list(@ModelAttribute("cmd") ListCommand listCommand,
			Errors errors, Model model) {
		if(errors.hasErrors()) {
			return "member/memberList";
		}
		if(listCommand.getFrom() != null && listCommand.getTo() != null) { //from, to 값을 입력하지 않으면 null값이 저장됨. 그 때는 members를 추가하지 않음
			List<Member> members = memberDao.selectByRegdate(
					listCommand.getFrom(), listCommand.getTo());
			model.addAttribute("members", members);
		}
		return "member/memberList";
	}
	
}
