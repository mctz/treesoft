package org.springframework.base.system.utils;

public class UsernamePasswordCaptchaToken
{
    private static final long serialVersionUID = 1L;
    
    private String captcha;
    
    public String getCaptcha()
    {
        return this.captcha;
    }
    
    public void setCaptcha(String captcha)
    {
        this.captcha = captcha;
    }
    
    public UsernamePasswordCaptchaToken()
    {
    }
    
    public UsernamePasswordCaptchaToken(String username, char[] password, boolean rememberMe, String host, String captcha)
    {
        this.captcha = captcha;
    }
}
