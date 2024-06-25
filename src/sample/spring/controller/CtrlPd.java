package sample.spring.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import sample.spring.common.MyBatisSession;
import sample.spring.dto.ShopBuyDTO;
import sample.spring.dto.ShopCartDTO;
import sample.spring.dto.ShopInterestDTO;
import sample.spring.dto.ShopProductDTO;
import sample.spring.dto.ShopUserDTO;
import sample.spring.dto.ShopWikiDTO;

@Controller
public class CtrlPd {
	private String msg; // 알림창 메시지
	
	private int product_code; // 상품코드
	private String product_category; // 카테고리
	private String product_brand; // 브랜드
	private String product_name; // 상품명
	private int product_price; // 상품가격
	private String product_explain; // 상품설명
	private String product_seller; // 판매자
	private String product_img; // 상품이미지
	
	private int product_count; // 상품 개수
	
	private String query_category; // 카테고리 선택
	private String query_brand; // 브랜드 선택
	private String query_search; // 검색어
	
	private String user_id; // 세션 ID
	
	
	// MyBatis
	String resource = "sample/spring/shopMapper/mybatis-config.xml";
	SqlSession sqlSession = MyBatisSession.getSession(resource);
	
	// 랜덤 상품 코드 6개 반환
	public int[] randomProduct(ArrayList<Integer> codeList) {
		int size = (codeList.size() < 6) ?
					codeList.size() : 6;
		int[] codes = new int[size];
		codes[0] = codeList.get((int)(Math.random() * codeList.size()));
		label : for(int outer = 1; outer < codes.length; outer++) {
			codes[outer] = codeList.get((int)(Math.random() * codeList.size()));
			for(int inner = outer - 1; inner >= 0; inner--) {
				//System.out.println(codes[outer]);
				if(codes[outer] == codes[inner]) {
					outer--;
					continue label;
				}
			}
		}
		return codes;
	}
	
	// 인덱스, 메인페이지 => 작동 O
	@RequestMapping(value = {"/index", "/mainPage"}, method = RequestMethod.GET)
	public ModelAndView mainPage(HttpSession session) {
		HashMap<String, ArrayList<ShopProductDTO>> mainMap = new HashMap<String, ArrayList<ShopProductDTO>>();
		
		// 랜덤 상품 리스트 =========================================================
		ArrayList<ShopProductDTO> randomList = new ArrayList<ShopProductDTO>();
		ArrayList<Integer> mb_randomCodes = (ArrayList)sqlSession.selectList("product_codes");
		try {
			int[] randomProductCode = randomProduct(mb_randomCodes);
			for(int idx = 0; idx < randomProductCode.length; idx++) {
				ShopProductDTO randomDTO = new ShopProductDTO();
				randomDTO.setProduct_code(randomProductCode[idx]);
				randomDTO = (ShopProductDTO)sqlSession.selectOne("getProduct", randomDTO);
				randomList.add(randomDTO);
			}
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		mainMap.put("randomList", randomList);
		
		// 구매율 TOP 5 상품 리스트 ===============================================
		ArrayList<ShopProductDTO> top5Products = new ArrayList<ShopProductDTO>();
		ArrayList<Integer> top5Code = (ArrayList)sqlSession.selectList("getTop5");
		for(int idx = 0; idx < top5Code.size(); idx++) {
			ShopProductDTO top5DTO = new ShopProductDTO();
			top5DTO.setProduct_code(top5Code.get(idx));
			top5DTO = (ShopProductDTO)sqlSession.selectOne("getProduct", top5DTO);
			top5Products.add(top5DTO);
		}
		mainMap.put("top5Products", top5Products);
		
		// 관심상품 ===============================================
		ShopInterestDTO iParam = new ShopInterestDTO();
		if(session.getAttribute("user_id") == null) user_id = "";
		else user_id = (String)session.getAttribute("user_id");
		iParam.setInterest_id(user_id);
		ArrayList<ShopInterestDTO> interestItems = (ArrayList)sqlSession.selectList("interestList", iParam);
		ArrayList<ShopProductDTO> interests = new ArrayList<ShopProductDTO>(); // 관심상품 리스트
		for(int idx = 0; idx < interestItems.size(); idx++) {
			ShopProductDTO iDTO = new ShopProductDTO();
			iDTO.setProduct_code(interestItems.get(idx).getInterest_product_code());
			iDTO = (ShopProductDTO)sqlSession.selectOne("getProduct", iDTO);
			interests.add(iDTO);
		}
		mainMap.put("interests", interests);
		
		// 특정 가격 이하 상품 리스트
		ShopProductDTO lessDTO = new ShopProductDTO();
		lessDTO.setProduct_price(100000);
		ArrayList<ShopProductDTO> lessThanPrice = (ArrayList)sqlSession.selectList("lessThanPrice", lessDTO);
		mainMap.put("lessThanPrice", lessThanPrice);
		
		//sqlSession.close(); // 메모리 누수현상 방지
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainMap", mainMap);
		mav.setViewName("shop/shop_main");
		return mav;
	}
	
	
	// 카테고리, 브랜드 선택 페이지 => 작동 O
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public ModelAndView category(@RequestParam Map<String, Object> map) {
		query_category = map.get("category").toString();

		HashMap<String, Object> categoryMap = new HashMap<String, Object>();
		
		// 상품 리스트
		ShopProductDTO categoryPds = new ShopProductDTO();
		categoryPds.setProduct_category(query_category);
		ArrayList<ShopProductDTO> items = (ArrayList)sqlSession.selectList("categorySel", categoryPds);
		if(map.get("brand") != null) {
			categoryPds.setProduct_brand(map.get("brand").toString());
			items = (ArrayList)sqlSession.selectList("categoryToBrandSel", categoryPds);
		}
		categoryMap.put("items", items);
		
		// 브랜드 리스트
		ArrayList<String> brands = (ArrayList)sqlSession.selectList("categories", categoryPds);
		categoryMap.put("brands", brands);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("categoryMap", categoryMap);
		mav.setViewName("shop/shop_category");
		return mav;
	}
	
	// 상품 설명 페이지 => 작동 O
	@RequestMapping(value = "/explain", method = RequestMethod.GET)
	public ModelAndView explain(@RequestParam Map<String, Object> map, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		HashMap<String, Object> explainMap = new HashMap<String, Object>();
		product_code = Integer.parseInt(map.get("product_code").toString());
		if(product_code == 0) { // 상품코드가 존재하지 않으면 메인 반환
			mav.setViewName("shop/shop_main");
			return mav;
		}
		// 상품 정보 DTO 반환 =================================
		ShopProductDTO explainDTO = new ShopProductDTO();
		explainDTO.setProduct_code(product_code);
		explainDTO = (ShopProductDTO)sqlSession.selectOne("getProduct", explainDTO);
		explainMap.put("explainDTO", explainDTO);
		
		// 상품 위키 반환 ====================================
		ShopWikiDTO findWiki = new ShopWikiDTO();
		findWiki.setWiki_code(product_code);
		findWiki = (ShopWikiDTO)sqlSession.selectOne("getWikiOfCode", findWiki);
		String wiki_text = (findWiki != null) ?
				findWiki.getWiki_text() : "";
		explainMap.put("wiki_text", wiki_text);

		// 관심상품 상태 반환
		user_id = (String)session.getAttribute("user_id");
		if(user_id != null) {
			ShopInterestDTO iStateDTO = new ShopInterestDTO();
			iStateDTO.setInterest_id(user_id);
			iStateDTO.setInterest_product_code(product_code);
			ArrayList<ShopInterestDTO> iStateList = (ArrayList)sqlSession.selectList("interestState", iStateDTO);
			int interestState = (iStateList.size() == 0) ? 0 : 1;
			explainMap.put("interestState", interestState);
		}
		
		mav.addObject("explainMap", explainMap);
		mav.setViewName("shop/shop_explain");
		return mav;
	}
	
	// 구매 페이지
	@RequestMapping(value = "/pageBuy", method = RequestMethod.GET)
	public ModelAndView pageBuy(@RequestParam Map<String, Object> map, HttpSession session) {
		HashMap<String, Object> buyMap = new HashMap<String, Object>();
		// 유저
		ShopUserDTO userDTO = new ShopUserDTO();
		userDTO.setUser_id((String)session.getAttribute("user_id"));
		userDTO = (ShopUserDTO)sqlSession.selectOne("userMyPageId", userDTO);
		
		// 상품
		ShopProductDTO product = new ShopProductDTO();
		product.setProduct_code(Integer.parseInt(map.get("product_code").toString()));
		product = sqlSession.selectOne("getProduct", product);
		
		buyMap.put("userDTO", userDTO);
		buyMap.put("product", product);
		buyMap.put("size", map.get("size").toString());
		buyMap.put("product_count", Integer.parseInt(map.get("product_count").toString()));
		buyMap.put("product_img", map.get("product_img").toString());
		ModelAndView mav = new ModelAndView();
		mav.addObject("buyMap", buyMap);
		mav.setViewName("shop/shop_buy");
		return mav;
	}
	
	// 장바구니 페이지 => 작동 O
	@RequestMapping(value = "/cartList", method = RequestMethod.GET)
	public ModelAndView cartList(HttpSession session) {
		HashMap<String, Object> cartMap = new HashMap<String, Object>();
		
		// 장바구니 리스트 반환 ==========================
		ShopCartDTO userCart = new ShopCartDTO();
		userCart.setCart_id((String)session.getAttribute("user_id"));
		ArrayList<ShopCartDTO> items = (ArrayList)sqlSession.selectList("cartSel", userCart);
		ArrayList<ShopProductDTO> cartList = new ArrayList<ShopProductDTO>();
		for(int idx = 0; idx < items.size(); idx++) {
			int cartNo = items.get(idx).getCart_code();
			ShopProductDTO cartItem = new ShopProductDTO();
			cartItem.setProduct_code(cartNo);
			cartItem = (ShopProductDTO)sqlSession.selectOne("getProduct", cartItem);
			cartList.add(cartItem);
		}
		cartMap.put("items", items);
		cartMap.put("cartList", cartList);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("cartMap", cartMap);
		mav.setViewName("shop/shop_cart");
		return mav;
	}
	
	// 구매내역 페이지 => 작동 O
	@RequestMapping(value = "/buyList", method = RequestMethod.GET)
	public ModelAndView buyList(HttpSession session) {
		if(session.getAttribute("user_id") != null) {
			user_id = (String)session.getAttribute("user_id");
		}
		
		// 유저의 상품 리스트 반환
		ShopBuyDTO buyDTO = new ShopBuyDTO();
		buyDTO.setBuy_id(user_id);
		ArrayList<ShopBuyDTO> buyList = (ArrayList)sqlSession.selectList("buyList", buyDTO);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("buyList", buyList);
		mav.setViewName("shop/shop_buyList");
		return mav;
	}
	
	// 검색 자동완성 => 작동 O
	@RequestMapping(value = "/autoCompleteSearch", method = RequestMethod.GET)
	public void autoCompleteSearch(@RequestParam("search") String product_name, HttpServletResponse response)
			throws ServletException, IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String result = "";
		if(product_name != null) {
			try {
				ShopProductDTO searchDTO = new ShopProductDTO();
				searchDTO.setProduct_name(product_name);
				List<ShopProductDTO> items = sqlSession.selectList("autoCompleteName", searchDTO);
				for(int idx = 0; idx < items.size(); idx++) {
					if(idx == 0) result += items.get(idx).getProduct_name();
					result += "," + items.get(idx).getProduct_name();
				}
				out.print(result);
			} catch(IndexOutOfBoundsException e) {
				out.print("");
			} finally {
				result = "";
			}
		}
	}
	
	// 상품 검색 결과 페이지 => 작동 O
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam Map<String, Object> map) {
		query_search = map.get("search").toString();
		ShopProductDTO searchDTO = new ShopProductDTO();
		searchDTO.setProduct_name(query_search);
		ArrayList<ShopProductDTO> searchList = (ArrayList)sqlSession.selectList("searchProductName", searchDTO);
		ModelAndView mav = new ModelAndView();
		mav.addObject("searchList", searchList);
		mav.setViewName("shop/shop_search");
		return mav;
	}
	
	// 상품 등록 페이지 => 작동 O
	@RequestMapping(value = "/pageRaiseProduct", method = RequestMethod.GET)
	public ModelAndView pageRaiseProduct(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		ShopUserDTO userDTO = new ShopUserDTO();
		userDTO.setUser_id((String)session.getAttribute("user_id"));
		userDTO = sqlSession.selectOne("userMyPageId", userDTO);
		mav.addObject("userDTO", userDTO);
		mav.setViewName("shop/shop_raiseProduct");
		return mav;
	}
	
	// 처리 =======================================
	// 구매 처리 => 작동 O
	@RequestMapping(value = "/buy", method = RequestMethod.POST)
	public ModelAndView buy(@RequestParam Map<String, Object> map, HttpSession session) {
		user_id = (String)session.getAttribute("user_id");
		product_code = Integer.parseInt(map.get("buy_productcode").toString());
		product_name = map.get("buy_productname").toString();
		product_img = map.get("buy_productimg").toString();
		product_count = Integer.parseInt(map.get("buy_cnt").toString());
		product_price = Integer.parseInt(map.get("buy_price").toString());
		String buy_addr = map.get("buy_addr").toString();
		ShopBuyDTO buyDTO = new ShopBuyDTO(user_id, product_code, product_name,
				product_img, product_count, product_price, buy_addr);
		int su = sqlSession.insert("shopBuy", buyDTO);
		sqlSession.commit();
		msg = (su != 0) ?
				user_id + "님 구매 성공" : user_id + "님 구매 실패";
		
		HashMap<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("msg", msg);
		orderMap.put("buyDTO", buyDTO);
		
		ShopUserDTO userDTO = new ShopUserDTO();
		userDTO.setUser_id(buyDTO.getBuy_id());
		userDTO = (ShopUserDTO)sqlSession.selectOne("userMyPageId", userDTO);
		orderMap.put("userDTO", userDTO);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("orderMap", orderMap);
		mav.setViewName("shop/shop_order");
		return mav;
	}
	
	// 처리 =======================================
	// 장바구니 추가 => 작동 O
	@RequestMapping(value = "/addCart", method = RequestMethod.GET)
	public ModelAndView addCart(@RequestParam Map<String, Object> map, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(session.getAttribute("user_id") == null) {
			msg = "로그인이 필요한 서비스입니다.";
			mav.addObject("msg", msg);
			mav.setViewName("shop/shop_login");
			return mav;
		} else if(session.getAttribute("user_id") != null) {
			user_id = (String)session.getAttribute("user_id");
		}
		product_code = Integer.parseInt(map.get("product_code").toString());
		product_name = map.get("product_name").toString();
		product_img = map.get("product_img").toString();
		product_count = Integer.parseInt(map.get("product_count").toString());
		ShopCartDTO cartItem = new ShopCartDTO(user_id, product_code, product_name, product_img, product_count);
		int su = sqlSession.insert("cartIns", cartItem);
		sqlSession.commit();
		
		return cartList(session); // 처리 완료 시 카트 페이지 반환
	}
	
	// 처리 =======================================
	// 장바구니 삭제 => 작동 O
	@RequestMapping(value = "/delCart", method = RequestMethod.GET)
	public ModelAndView delCart(@RequestParam("product_code") int product_code, HttpSession session,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		ShopCartDTO cartDel = new ShopCartDTO();
		cartDel.setCart_id((String)session.getAttribute("user_id"));
		cartDel.setCart_code(product_code);
		int su = sqlSession.delete("cartDel", cartDel);
		sqlSession.commit();
		
		return cartList(session);
	}
	
	// 처리 =======================================
	// 관심상품 추가 => 작동 O
	@RequestMapping(value = "/addInterest", method = RequestMethod.GET)
	public void addInterest(@RequestParam Map<String, Object> map,
			HttpSession session, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(session.getAttribute("user_id") == null) {
			out.print("로그인이 필요한 서비스입니다.");
			return;
		} else {
			user_id = (String)session.getAttribute("user_id");
		}
		product_code = Integer.parseInt(map.get("product_code").toString());
		product_name = map.get("product_name").toString();
		product_img = map.get("product_img").toString();
		ShopInterestDTO interestDTO = new ShopInterestDTO(user_id, product_code, product_name, product_img);
		int su = sqlSession.insert("interestInsert", interestDTO);
		sqlSession.commit();
		
		if(su != 0)
			out.print("관심상품 등록 성공");
		else
			out.print("관심상품 등록 실패");
	}
	
	// 처리 =======================================
	// 관심상품 삭제 => 작동 O
	@RequestMapping(value = "/delInterest", method = RequestMethod.GET)
	public void delInterest(@RequestParam Map<String, Object> map,
			HttpSession session, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(session.getAttribute("user_id") == null) {
			out.print("로그인이 필요한 서비스입니다.");
			return;
		} else {
			user_id = (String)session.getAttribute("user_id");
		}
		product_code = Integer.parseInt(map.get("product_code").toString());
		ShopInterestDTO interestDel = new ShopInterestDTO();
		interestDel.setInterest_id(user_id);
		interestDel.setInterest_product_code(product_code);
		int su= sqlSession.delete("interestDel",interestDel );
		sqlSession.commit();
		
		if(su != 0) {
			out.print("관심상품 삭제 성공");
		} else {
			out.print("관심상품 삭제 실패");
		}
	}
	
	// 처리 =======================================
	// 위키 등록 OR 갱신 => 작동 O
	@RequestMapping(value = "/updateWiki", method = RequestMethod.GET)
	public void updateWiki(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String wiki_text = request.getParameter("wiki_text");
		product_code = Integer.parseInt(request.getParameter("product_code"));
		product_name = request.getParameter("product_name");
		ShopWikiDTO wikiDTO = new ShopWikiDTO(product_code, product_name, wiki_text);
		ArrayList<ShopWikiDTO> findWiki = (ArrayList)sqlSession.selectList("getWikiOfCode", wikiDTO);
		int su = 0;
		if(findWiki.size() != 0) {
			su = sqlSession.update("updateWiki", wikiDTO);
		} else {
			su = sqlSession.insert("insertWiki", wikiDTO);
		}
		sqlSession.commit();
		
		// 업데이트 후 이동
		if(su != 0)
			response.sendRedirect(request.getContextPath() + "/explain?product_code=" + product_code);
	}
	
	// 처리 =======================================
	// 상품 등록 => 작동 O
	@RequestMapping(value = "/raiseProduct", method = RequestMethod.POST)
	public void raiseProduct(@RequestParam Map<String, Object> map,
							HttpServletRequest request ,HttpServletResponse response)
			throws ServletException, IOException{
		ShopProductDTO productDTO = new ShopProductDTO();
		int maxNo = (int)sqlSession.selectOne("productMaxNo");
		productDTO.setProduct_code(maxNo + 1);
		productDTO.setProduct_category(map.get("product_category").toString());
		productDTO.setProduct_brand(map.get("product_brand").toString());
		productDTO.setProduct_name(map.get("product_name").toString());
		productDTO.setProduct_price(Integer.parseInt(map.get("product_price").toString()));
		productDTO.setProduct_explain(map.get("product_explain").toString());
		productDTO.setProduct_seller(map.get("product_seller").toString());
		productDTO.setProduct_img(map.get("product_img").toString());
		
		int su = sqlSession.insert("raiseProduct", productDTO);
		if(su != 0) {
			sqlSession.commit();
			msg = productDTO.getProduct_seller() + "님 상품 등록 완료";
		} else {
			msg = "상품 등록 실패";
		}
		
		response.sendRedirect(request.getContextPath() + "/mainPage?msg="+URLEncoder.encode(msg, "UTF-8"));
	}
}