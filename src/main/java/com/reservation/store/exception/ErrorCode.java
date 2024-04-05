package com.reservation.store.exception;

public record ErrorCode(int status, String message) {
    public static final ErrorCode DUPLICATE_USER = new ErrorCode(400, "이미 가입된 회원입니다.");
    public static final ErrorCode USER_NOT_FOUND = new ErrorCode(404, "사용자를 찾을 수 없습니다.");
    public static final ErrorCode NOT_PARTNER = new ErrorCode(403, "점장이 아니면 등록할 수 없습니다.");
    public static final ErrorCode DUPLICATE_STORE_LOCATION = new ErrorCode(400, "같은 위치에 이미 매장이 등록되어 있습니다.");
    public static final ErrorCode STORE_NOT_FOUND = new ErrorCode(404, "해당 가게의 정보가 없습니다.");
    public static final ErrorCode REVIEW_NOT_FOUND = new ErrorCode(404, "리뷰를 찾을 수 없습니다.");
    public static final ErrorCode RESERVATION_NOT_FOUND = new ErrorCode(404, "예약을 찾을 수 없습니다.");
    public static final ErrorCode UNAUTHORIZED_REVIEW_UPDATE = new ErrorCode(403, "리뷰 수정 권한이 없습니다.");
    public static final ErrorCode UNAUTHORIZED_REVIEW_DELETE = new ErrorCode(403, "리뷰 삭제 권한이 없습니다.");
    public static final ErrorCode REVIEW_ALREADY_EXISTS = new ErrorCode(409, "이미 리뷰를 작성했습니다.");
    public static final ErrorCode REVIEW_NOT_ALLOWED = new ErrorCode(400, "매장 이용 후에 리뷰를 달 수 있습니다.");
    public static final ErrorCode RESERVATION_TOO_SOON = new ErrorCode(400, "예약은 현재 시간으로부터 최소 10분 후부터 가능합니다.");
    public static final ErrorCode INVALID_ARRIVAL_TIME = new ErrorCode(400, "방문 확인이 불가능한 시간입니다.");
}
