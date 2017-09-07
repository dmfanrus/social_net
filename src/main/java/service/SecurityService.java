package service;

/**
 * Created by Михаил on 06.01.2017.
 */
public interface SecurityService {
    String encrypt(String password);

    boolean validate(String password, String hash);
}
