package com.example.authServerPassword.Controller;

import java.util.*;
import com.example.authServerPassword.Domain.Client;
import com.example.authServerPassword.Domain.ClientDto;
import com.example.authServerPassword.Service.ClientDetailsServiceImpl;
import com.example.authServerPassword.constrant.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@Controller
@RequestMapping("/client")
public class clientsController {
    @Autowired
    private ClientDetailsServiceImpl clientRegistrationService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public ModelAndView registerPage(ModelAndView mav) {
        mav.setViewName("client/register");
        mav.addObject("registry", new ClientDto());
        return mav;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(@ModelAttribute("clientId")String clientId, @ModelAttribute("clientSecret")String clientSecret, @ModelAttribute("name")String name
            , @ModelAttribute("clientType")String clientType
            , ModelAndView mv) {

        if(!StringUtils.isEmpty(clientId)) {
            mv.addObject("applications", clientRegistrationService.loadClientByClientId(clientId));
            mv.addObject("client_secret", clientSecret);
        }
        mv.setViewName("client/dashboard");
        return mv;
    }

    @Transactional
    @PostMapping("/save")
    public ModelAndView save(ClientDto clientDetails, ModelAndView mav , BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return new ModelAndView("/client/register");
        }

        Collection<String> grant = new ArrayList<String>();
        grant.add("password");
        grant.add("client_credentials");
        grant.add("refresh_token");
        String randomId = UUID.randomUUID().toString();
        String randomSecret = UUID.randomUUID().toString();

        String name = clientDetails.getName();
        // token_type : access : 0 (false)
        //              jwt : 1 (true)

        Client client = new Client();
        client.addAdditionalInformation("name", clientDetails.getName());
        client.setRegisteredRedirectUri(new HashSet<>(Arrays.asList(clientDetails.getRedirectUri().toString())));
        client.setClientType(ClientType.PUBLIC);
        client.setAuthorizedGrantTypes(grant);
        client.setClientId(randomId);
        client.setTokenType(false);
        client.setClientSecret(passwordEncoder.encode(randomSecret));
        client.setAccessTokenValiditySeconds(3600);
        client.setScope(Arrays.asList("read","write"));
        clientRegistrationService.addClientDetails(client);

        mav.setViewName("redirect:/client/dashboard");
        mav.addObject("clientId", randomId);
        mav.addObject("clientSecret", randomSecret);
        mav.addObject("name", name);
        mav.addObject("clientType", clientDetails.getClientType());

        return mav;
    }


    @GetMapping("/remove")
    public ModelAndView remove(
            @RequestParam(value = "client_id", required = false) String clientId) {

        clientRegistrationService.removeClientDetails(clientId);

        ModelAndView mv = new ModelAndView("redirect:/client/dashboard");
        mv.addObject("applications", clientRegistrationService.listClientDetails());
        return mv;
    }
}
