package sample.spring.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sample.spring.common.MyBatisSession;
import sample.spring.dto.ShopNotificationDTO;

@Controller
public class CtrlNotification {
    String resource = "sample/spring/shopMapper/mybatis-config.xml";
    SqlSession sqlSession = MyBatisSession.getSession(resource);

    // 메인화면
    @RequestMapping(value = "/shop_notification", method = RequestMethod.GET)
    public ModelAndView notification() {
        ArrayList<ShopNotificationDTO> dtoL = new ArrayList<>();
        dtoL = (ArrayList) sqlSession.selectList("bodSel", dtoL);
        ModelAndView mav = new ModelAndView();
        mav.addObject("dtoL", dtoL);
        mav.setViewName("shop/shop_notification");
        return mav;
    }

    // 글 작성페이지
    @RequestMapping(value = "/shop_notification_write", method = RequestMethod.GET)
    public ModelAndView notification_write(HttpSession session) {
        ModelAndView mav = new ModelAndView();

        // 세션에서 사용자 ID 가져오기
        String user_id = (String) session.getAttribute("user_id");

        // 세션에 user_id가 없으면 로그인 페이지로 리다이렉트
        if (user_id == null) {
            mav.setViewName("redirect:/login?msg=" + encodeUrl("로그인이 필요한 서비스입니다"));
            return mav;
        }

        mav.setViewName("shop/shop_notification_write");
        return mav;
    }

    // 작성 처리 페이지
    @RequestMapping(value = "/shop_notification_writepro", method = RequestMethod.GET)
    public ModelAndView notification_writepro(@RequestParam Map<String, Object> map, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");

        // 세션에 user_id가 없으면 로그인 페이지로 리다이렉트
        if (user_id == null) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/shop_login?msg=" + encodeUrl("로그인이 필요한 서비스입니다"));
            return mav;
        }

        int maxNum = sqlSession.selectOne("bodMaxnum");
        String notification_Subject = map.get("notification_Subject").toString();
        String notification_Content = map.get("notification_Content").toString();
        int notification_No = maxNum + 1;

        ShopNotificationDTO notificationDTO = new ShopNotificationDTO();
        notificationDTO.setNotification_Id(user_id);
        notificationDTO.setNotification_Content(notification_Content);
        notificationDTO.setNotification_Subject(notification_Subject);
        notificationDTO.setNotification_No(notification_No);

        int su = sqlSession.insert("bodIns", notificationDTO);
        sqlSession.commit();

        return new ModelAndView("redirect:/shop_notification");
    }

    // 게시판 확인
    @RequestMapping(value = "/shop_notification_content", method = RequestMethod.GET)
    public ModelAndView shop_notification_content(@RequestParam Map<String, Object> map) {
        int notification_No = Integer.parseInt(map.get("notification_No").toString());
        ArrayList<ShopNotificationDTO> dtoL = new ArrayList<>();
        dtoL = (ArrayList) sqlSession.selectList("bodSel", dtoL);
        ModelAndView mav = new ModelAndView();
        mav.addObject("dtoL", dtoL);
        mav.setViewName("shop/shop_notification_content");
        return mav;
    }

    // 게시판 삭제
    @RequestMapping(value = "/shop_notification_delete", method = RequestMethod.GET)
    public ModelAndView notification_delete(@RequestParam Map<String, Object> map, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");

        if (user_id == null) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/login?msg=" + encodeUrl("로그인이 필요한 서비스입니다"));
            return mav;
        }

        int notification_No = Integer.parseInt(map.get("notification_No").toString());
        ShopNotificationDTO notificationDTO = sqlSession.selectOne("getbod", notification_No);
        String id = notificationDTO.getNotification_Id();

        if (id.equals(user_id)) {
            int su = sqlSession.delete("bodDel", notificationDTO);
            sqlSession.commit();
        }

        return new ModelAndView("redirect:/shop_notification");
    }

    // 게시판 수정페이지 이동
    @RequestMapping(value = "/shop_notification_update", method = RequestMethod.GET)
    public ModelAndView notification_update(@RequestParam Map<String, Object> map, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");
        int notification_No = Integer.parseInt(map.get("notification_No").toString());
        ShopNotificationDTO notificationDTO = sqlSession.selectOne("getbod", notification_No);
        String id = notificationDTO.getNotification_Id();
        if (user_id == null) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/login?msg=" + encodeUrl("로그인이 필요한 서비스입니다"));
            return mav;
        }else if(id.equals(user_id)) {            
            ArrayList<ShopNotificationDTO> dtoL = new ArrayList<ShopNotificationDTO>();
    		dtoL = (ArrayList)sqlSession.selectList("getbod", notification_No);
    		ModelAndView mav = new ModelAndView();
            mav.addObject("dtoL", dtoL);
            mav.addObject("notification_No", notification_No);
            mav.setViewName("shop/shop_notification_update");
            return mav;
        }else {        	      	 
        	 ArrayList<ShopNotificationDTO> dtoL = new ArrayList<ShopNotificationDTO>();
     		 dtoL = (ArrayList)sqlSession.selectList("getbod", notification_No);
     		 ModelAndView mav = new ModelAndView();
     		 mav.addObject("dtoL", dtoL);
        	 mav.addObject("notification_No", notification_No);
        	 mav.setViewName("shop/shop_notification_content");
             return mav;
        }
    }

    // 게시판 수정
    @RequestMapping(value = "/shop_notification_updatepro", method = RequestMethod.GET)
    public ModelAndView notification_updatepro(@RequestParam Map<String, Object> map, HttpSession session) {
        String user_id = (String) session.getAttribute("user_id");

        if (user_id == null) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/login?msg=" + encodeUrl("로그인이 필요한 서비스입니다"));
            return mav;
        }

        int notification_No = Integer.parseInt(map.get("notification_No").toString());
        String notification_Subject = map.get("notification_Subject").toString();
        String notification_Content = map.get("notification_Content").toString();

        ShopNotificationDTO notificationDTO = new ShopNotificationDTO();
        notificationDTO.setNotification_Id(user_id);
        notificationDTO.setNotification_Content(notification_Content);
        notificationDTO.setNotification_Subject(notification_Subject);
        notificationDTO.setNotification_No(notification_No);

        int result = sqlSession.update("bodUpd", notificationDTO);
        sqlSession.commit();

        return new ModelAndView("redirect:/shop_notification");
    }

    // URL 인코딩 메서드
    private String encodeUrl(String msg) {
        try {
            return URLEncoder.encode(msg, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
