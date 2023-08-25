package com.salessparrow.api.services.crmOrganizationUsers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetCrmOrganizationUsersFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetCrmOrganizationUsersDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.crmActions.getCrmOrganizationUsers.GetCrmOrganizationUsersFactory;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetCrmOrganizationUsersList class for the getCrmOrganizationUsers action.
 * 
 * 
 */
@Service
public class GetCrmOrganizationUsersList {

    Logger logger = LoggerFactory.getLogger(GetCrmOrganizationUsersList.class);
    
    @Autowired
    private GetCrmOrganizationUsersFactory getCrmOrganizationUsersFactory;

    /**
     * getCrmOrganizationUsers method for the getCrmOrganizationUsers action.
     * 
     * @param request
     * @param crmOrganizationUsersDto
     * 
     * @return GetCrmOrganizationUsersFormatterDto
    */
    public GetCrmOrganizationUsersFormatterDto getCrmOrganizationUsers(HttpServletRequest request ,GetCrmOrganizationUsersDto crmOrganizationUsersDto) {
        logger.info("Inside Search crm organization user service");
        User currentUser = (User) request.getAttribute("current_user");
        String formattedSearchString = "";
        if(crmOrganizationUsersDto.getQ() != null){
            formattedSearchString = formatSearchString(crmOrganizationUsersDto.getQ());
        }

        return getCrmOrganizationUsersFactory.getCrmOrganizationUsers(currentUser, formattedSearchString);
    }

    /**
     * formatSearchString method for the getCrmOrganizationUsers action.
     * 
     * @param searchTerm
     * 
     * @return String
     */
    private String formatSearchString(String searchTerm) {
        logger.info("format and sanitize search term");
        searchTerm = searchTerm.trim();
        try {
            return URLEncoder.encode(searchTerm,"UTF-8");
        } catch (UnsupportedEncodingException e) {

            List<String> paramError = new ArrayList<>();
            paramError.add("invalid_search_term");

            throw new CustomException(
                new ParamErrorObject(
                "s_cou_gcoul_fss",
                e.getMessage(),
                paramError
                )
            );
        }
    }

}
