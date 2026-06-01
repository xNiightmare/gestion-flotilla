package com.grandedev.gestionflotilla.auth;

import com.grandedev.gestionflotilla.login.LoginRequestDTO;

public interface IAuthService {
    String login(LoginRequestDTO loginRequestDTO);
}
