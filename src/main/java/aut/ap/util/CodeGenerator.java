package aut.ap.util;

import aut.ap.dao.EmailDao;
import java.security.SecureRandom;

public class CodeGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode(EmailDao emailDao) {
        String code;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;
        do {
            if (attempts++ >= MAX_ATTEMPTS) {
                throw new RuntimeException("Failed to generate unique code after " + MAX_ATTEMPTS + " attempts");
            }
            StringBuilder sb = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            code = sb.toString();
        } while (emailDao.findByCode(code) != null);
        return code;
    }
}