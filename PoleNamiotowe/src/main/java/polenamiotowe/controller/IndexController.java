package polenamiotowe.controller;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import repositories.*;

@Controller
public class IndexController {

    UzytkownikRepository uzytkownikRespository;

    PoleRespository poleRespository;


    public IndexController() {
        uzytkownikRespository = new UzytkownikRepository();
        poleRespository = new PoleRespository();
    }

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/login")
    public ModelAndView login(Model model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        String user = request.getParameter("usr");
        String password = request.getParameter("pwd");

        try {
            String userId = uzytkownikRespository.WeryfikujLoginHaslo(user, password);
            if (userId != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId);
                session.setMaxInactiveInterval(60);

                mav.setViewName("lista");
                return mav;
            }
        } catch (SQLException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }

        mav.setViewName("rejestracja");
        mav.addObject("blad", 1);
        return mav;
    }

    @RequestMapping("/kontakt")
    public ModelAndView kontakt(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("kontakt");
        mav.addObject("parametr", 5);
        return mav;
    }

    @RequestMapping(value = "/rejestracja", method = RequestMethod.GET)
    public ModelAndView rejestracjaGet(Model model, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("rejestracja");

        String user = request.getParameter("usr");
        String password = request.getParameter("pwd");

        try {

            if (!(password == null && user == null)) {
                if (uzytkownikRespository.UzytkownikIstnieje(user, password)) {
                    mav.addObject("blad", "U�ytkownik jest ju� zaj�ty!!");

                } else {
                    uzytkownikRespository.RejestrujUzytkownika(user, password);
                    mav.setViewName("index");
                }

                return mav;
            }
        } catch (SQLException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mav;
    }

    @RequestMapping(value = "/rejestracja", method = RequestMethod.POST)
    public ModelAndView rejestracjaPost(Model model, @RequestParam(value = "pwd", required = false) String pwd) {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("rejestracja");
        return mav;
    }

    @RequestMapping(value = "/rezerwacjaMiejsca", method = RequestMethod.GET)
    public ModelAndView rezerwacjaMiejscaGet(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("RezerwacjaMiejsca");
        return mav;
    }

    @RequestMapping(value = "/lista", method = RequestMethod.GET)
    public ModelAndView listaGet(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("lista");
        return mav;
    }

    @RequestMapping(value = "/dodawaniePola", method = RequestMethod.GET)

    public ModelAndView dodawaniePolaGet(Model model, HttpServletRequest request) throws SQLException {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("DodawaniePola");
        String adres = request.getParameter("adres");

        if (!(adres == null)) {

            if (poleRespository.istniejePole(adres)) {
                mav.addObject("blad", "Pole ju� istnieje!");
            } else {
                poleRespository.dodajPole(adres);
            }

        }
        return mav;
    }

    
    @RequestMapping(value = "/DodawaniePola", method=RequestMethod.GET)
    public ModelAndView DodawaniePolaGet(Model model) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("DodawaniePola");
        return mav;
    }

}
