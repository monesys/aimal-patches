package app.aimal.extension.streaming;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Sanitizes Apollo GraphQL's CheckQuery result for Disney+ (OneID).
 * Replaces OTP and OTP_LOGIN operations with Login so that the app directly
 * presents the password entry screen, allowing Google Password Manager to auto-fill.
 */
public final class LoginBridge {
    private LoginBridge() {
    }

    /**
     * Sanitizes the deserialized Check data class instance (e.g. zh0.m).
     * Modifies the operations list to prioritize Login over OTP and ensures
     * nextOperation is set to Login unless it is a new account registration.
     *
     * @param checkResult the deserialized Check data class instance
     * @return the modified checkResult (or original object if reflection fails)
     */
    public static Object sanitizeCheckResult(Object checkResult) {
        if (checkResult == null) {
            return null;
        }

        try {
            Class<?> clazz = checkResult.getClass();
            Field[] fields = clazz.getDeclaredFields();

            Field operationsField = null;
            Field nextOperationField = null;

            for (Field f : fields) {
                f.setAccessible(true);
                Object val = f.get(checkResult);
                if (val instanceof List) {
                    operationsField = f;
                } else if (val instanceof Enum) {
                    nextOperationField = f;
                }
            }

            // 1. Sanitize operations list
            if (operationsField != null) {
                Object listObj = operationsField.get(checkResult);
                if (listObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Object> list = (List<Object>) listObj;
                    if (!list.isEmpty()) {
                        Object firstItem = list.get(0);
                        if (firstItem instanceof Enum) {
                            Class<?> enumClass = firstItem.getClass();
                            Object loginEnum = findEnumConstant(enumClass, "Login", "LOGIN");

                            if (loginEnum != null) {
                                boolean modified = false;
                                for (int i = 0; i < list.size(); i++) {
                                    Object item = list.get(i);
                                    if (item != null) {
                                        String name = item.toString();
                                        if (name.equalsIgnoreCase("OTP") || name.equalsIgnoreCase("OTP_LOGIN")) {
                                            list.set(i, loginEnum);
                                            modified = true;
                                        }
                                    }
                                }
                                if (!list.contains(loginEnum)) {
                                    list.add(0, loginEnum);
                                    modified = true;
                                }
                                if (modified) {
                                    Logger.i("LoginBridge: Successfully sanitized operations list to prioritize Login");
                                }
                            }
                        }
                    }
                }
            }

            // 2. Sanitize nextOperation field
            if (nextOperationField != null) {
                Object nextOp = nextOperationField.get(checkResult);
                if (nextOp instanceof Enum) {
                    Class<?> enumClass = nextOp.getClass();
                    Object loginEnum = findEnumConstant(enumClass, "Login", "LOGIN");
                    if (loginEnum != null) {
                        String currentName = nextOp.toString();
                        if (!currentName.equalsIgnoreCase("Login") &&
                                !currentName.equalsIgnoreCase("REGISTER") &&
                                !currentName.equalsIgnoreCase("RegisterAccount")) {
                            nextOperationField.set(checkResult, loginEnum);
                            Logger.i("LoginBridge: Successfully forced nextOperation to Login (was: " + currentName + ")");
                        }
                    }
                }
            }

        } catch (Throwable t) {
            Logger.e("LoginBridge: Failed to sanitize checkResult", t);
        }

        return checkResult;
    }

    private static Object findEnumConstant(Class<?> enumClass, String... candidateNames) {
        Object[] constants = enumClass.getEnumConstants();
        if (constants == null) return null;
        for (Object c : constants) {
            String name = c.toString();
            for (String candidate : candidateNames) {
                if (name.equalsIgnoreCase(candidate)) {
                    return c;
                }
            }
        }
        return null;
    }
}
