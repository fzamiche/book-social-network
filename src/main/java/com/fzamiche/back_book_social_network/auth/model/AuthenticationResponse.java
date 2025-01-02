package com.fzamiche.back_book_social_network.auth.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationResponse {

    private  String jwtToken;
}
