package cn.faury.android.library.common.core;

/**
 * 权限异常
 */

public class PermissionException extends RuntimeException {
    /**
     * 权限字符串
     */
    private String permission;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public PermissionException(String permission) {
        this.permission = permission;
    }

    /**
     * Constructs a new runtime exception with the specified cause and a
     * detail message of <tt>(cause==null ? null : cause.toString())</tt>
     * (which typically contains the class and detail message of
     * <tt>cause</tt>).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public PermissionException(String permission, Throwable cause) {
        super(cause);
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
