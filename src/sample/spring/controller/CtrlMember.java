package sample.spring.controller;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sample.spring.common.MyBatisSession;
import sample.spring.dto.ShopUserDTO;

@Controller
public class CtrlMember {
	
	private String id="";
	private String pwd="";
	private String name="";
	private String email="";
	private String phone="";
	private String addr="";
	private String rank="";
	
	private String column="";
	private String up_column="";
	private String userDel="";
	private String check="";
	
	private String msg="";
	private int su=0;
	
	SqlSession sqlSession = MyBatisSession.getSession("sample/spring/shopMapper/mybatis-config.xml");
	
	/*public SqlSession myBatis() throws ServletException , IOException{
		Reader reader = Resources.getResourceAsReader("sample/spring/shopMapper/mybatis-config.xml");
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		SqlSession session1 = sessionFactory.openSession();
		return session1;
	}*/
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shop/shop_login");
		return mav;
	}
	
	@RequestMapping(value = "/membership", method = RequestMethod.POST)
	public ModelAndView membership(@RequestParam Map<String, Object> map) {
		//phone = map.get("user_phone").toString();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shop/shop_member");
		return mav;
	}
	
	@RequestMapping(value = "/phoneSms", method = RequestMethod.POST)
	public ModelAndView phoneSms() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shop/shop_phoneSms");
		return mav;
	}
	@RequestMapping(value = "/pageIdfind", method = RequestMethod.POST)
	public ModelAndView idfind() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shop/shop_idfind");
		return mav;
	}
	@RequestMapping(value = "/pagePwdfind", method = RequestMethod.POST)
	public ModelAndView passwordfind() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shop/shop_passwordfind");
		return mav;
	}
	@RequestMapping(value = "/pageSms", method = RequestMethod.POST)
	public ModelAndView pageSms() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("shop/shop_sms");
		return mav;
	}
	
	
	//로그인 id,pwd
	@RequestMapping(value = "/frmLogin", method = RequestMethod.POST)
	public ModelAndView frmLogin(@RequestParam Map<String, Object> map, HttpSession session)
			throws ServletException , IOException {
		this.id = map.get("user_id").toString();
		pwd = map.get("user_pwd").toString();
		ShopUserDTO userDTO = new ShopUserDTO(this.id, pwd);
		
		//SqlSession sqlSession = myBatis();
		
		ArrayList<ShopUserDTO> sessionArr = (ArrayList)sqlSession.selectList("userLogAll",userDTO);
		
		ModelAndView mav = new ModelAndView();

		if(sessionArr.size()!=0) {
			session.setAttribute("user_id", map.get("user_id").toString());
			return new CtrlPd().mainPage(session);
		} else {
			msg = "로그인 실패\n아이디 또는 비밀번호 오류입니다";
			mav.setViewName("shop/shop_login");
		}
		//mav.addObject("user_id",this.id);
		mav.addObject("msg",msg);
		sqlSession.commit();
		return mav;
	}
	
	//로그아웃
	@RequestMapping(value = "/frmLogout", method = RequestMethod.GET)
	public ModelAndView frmLogout(HttpSession session) {
		session.removeAttribute("user_id");
		return new CtrlPd().mainPage(session);
	}
	
	//id 찾기
	@RequestMapping(value = "/frmIdFine", method = RequestMethod.POST)
	public ModelAndView frmIdFine(@RequestParam Map<String, Object> map) throws ServletException , IOException {
		phone = map.get("shop_phone").toString();
		ShopUserDTO userDTO = new ShopUserDTO();
		userDTO.setUser_phone(phone);
		//SqlSession sqlSession = myBatis();
		
		ArrayList<ShopUserDTO> sessionArr = (ArrayList)sqlSession.selectList("userFind", userDTO);
		sqlSession.commit();
		if(sessionArr != null) {
			msg = ("아이디는" + sessionArr.get(0).getUser_id() + "입니다.");
		}else {
			msg = ("\n\t^존재하지 않는 회원 입니다♬");
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg",msg);
		mav.setViewName("shop/shop_login");
		return mav;
	}
	
	//pwd 찾기
	@RequestMapping(value = "/frmPwdFine", method = RequestMethod.POST)
	public ModelAndView frmPwdFine(@RequestParam Map<String, Object> map) throws ServletException , IOException {
		id = map.get("shop_id").toString();
		phone = map.get("shop_phone").toString();
		ShopUserDTO userDTO = new ShopUserDTO();
		userDTO.setUser_id(id);
		userDTO.setUser_phone(phone);
		//SqlSession sqlSession = myBatis();
		
		ArrayList<ShopUserDTO> sessionArr = (ArrayList)sqlSession.selectList("userPwdFind",userDTO);
		sqlSession.commit();
		if (sessionArr != null) {
			msg = ("비밀번호는" + sessionArr.get(0).getUser_pwd() + "입니다.");
		} else {
			msg = ("\n\t^존재하지 않는 회원 입니다♬");
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg",msg);
		mav.setViewName("shop/shop_login");
		return mav;
	}
	
	//myPage
	@RequestMapping(value = "/myPage", method = RequestMethod.GET)
	public ModelAndView frmMyPage(@RequestParam Map<String, Object> map, HttpSession session) throws ServletException , IOException {
		ShopUserDTO userDTO = new ShopUserDTO();
		userDTO.setUser_id((String)session.getAttribute("user_id"));
		
		String vUrl = "shop/shop_myPage";
		
		if(map.get("column") !=null) {
			System.out.println("test------------------------------------------");
			column = map.get("column").toString();
			up_column = map.get("up_column").toString();
		}
		if(map.get("check") != null) {
			check = map.get("check").toString();
		}
		if(map.get("userDel") != null) {
			userDel = map.get("userDel").toString();
		}
		System.out.println(column + " / " + up_column);
		//SqlSession sqlSession = myBatis();
		
		ArrayList<ShopUserDTO> userArr = (ArrayList)sqlSession.selectList("userMyPageId", userDTO);
		
		String rank[] = userArr.get(0).getUser_rank().split("1");
		
		/*광고 동의 선택 시 실행*/
		if(check!=null){
			  userDTO = new ShopUserDTO();
			  userDTO.setUser_id(id);
			  userDTO.setUser_pwd("user_rank");
			  userDTO.setUser_name(rank[0] + "1" + check);
			  su = sqlSession.update("userMyPageUp",userDTO);
		  }
		
		  /*변경할 시 실행*/
		  if(column!=null || up_column!=null){
			  if(column!="" || up_column!="") {
				userDTO = new ShopUserDTO();
				userDTO.setUser_id(id);
				userDTO.setUser_pwd(column);
				userDTO.setUser_name(up_column);
			  	su = sqlSession.update("userMyPageUp",userDTO);
			  }
		  }
		  
		  /*삭제 오류 방지*/
		  if(userDel==null){
			  userDel = "0";
		  }
		  
		  /*삭제 시 실행*/
		  if(userDel.equals("1")){
			  su = sqlSession.delete("userMyPageDel",userDTO);
			  //vUrl = "shop/shop_main";
			  //this.id = null;
			  session.removeAttribute("user_id");
			  return new CtrlPd().mainPage(session);
		  }
		  ModelAndView mav = new ModelAndView();
			mav.addObject("user_id",this.id);
			mav.setViewName(vUrl);
		sqlSession.commit();
		return mav;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(@RequestParam Map<String, Object> map) {
		String user_id = map.get("user_id").toString();
		String user_pwd = map.get("user_pwd").toString();
		String user_name = map.get("user_name").toString();
		String user_email = map.get("user_email").toString();
		String user_phone = map.get("user_phone").toString();
		String user_addr = map.get("user_addr").toString();
		String user_rank = map.get("user_rank").toString();
		
		ShopUserDTO userDTO = new ShopUserDTO(user_id, user_pwd, user_name, user_email, user_phone, user_addr, user_rank);
		int su = sqlSession.insert("userRegister", userDTO);
		sqlSession.commit();
		if(su != 0) {
			if(su == -1)
				msg = "이미 존재하는 아이디입니다.";
			else
				msg = userDTO.getUser_name() + "님 회원가입에 성공하셨습니다.";
		} else {
			msg = "회원가입에 실패하셨습니다.";
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("msg", msg);
		mav.setViewName("shop/shop_login");
		return mav;
	}
}