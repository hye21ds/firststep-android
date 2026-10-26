package com.example.firststep;

public final class MissionData {
    private MissionData() {}
    public static final String[] PLACES = {
        "수업 건물 주변", "중앙도서관 주변", "학생회관 주변", "야외 탐방 지점", "체육시설 주변"
    };
    public static final String[] TITLES = {
        "첫 수업의 문을 찾다", "배움의 기록을 밝히다", "캠퍼스 생활의 목소리", "나의 방향을 찾다", "캠퍼스의 하루를 완성하다"
    };
    public static final String[] INPUTS = {"사진", "조도센서", "음성", "방향센서", "가속도센서"};
    public static final String[] STORIES = {
        "첫 수업에 필요한 건물을 찾아보자. 현장의 건물명 표식을 관찰하고 사진으로 기록하는 미션이야.",
        "도서관의 숨겨진 문장을 찾아보자. 휴대전화의 조도센서를 가렸다가 다시 밝게 만들어 기록을 복원해 줘.",
        "대학생활에 필요한 장소를 알아보자. 안내를 듣고 시설의 역할에 대해 말하거나 글로 답하는 미션이야.",
        "지도 위의 점과 실제 공간을 연결해 보자. 제자리에서 휴대전화를 돌려 목표 방향을 찾는 미션이야.",
        "마지막 기억은 움직임이야. 휴대전화를 가볍게 다섯 번 움직여 활력 게이지를 채워 줘."
    };
    public static final String[] LEARNING = {
        "건물명과 출입구 표식을 확인해 위치를 기억한다.",
        "도서관의 위치와 이용 목적을 기억한다.",
        "학생회관 등 생활 시설의 역할을 이해한다.",
        "서로 다른 시설의 상대적 방향을 익힌다.",
        "활동·휴식 공간이 어디에 있는지 익힌다."
    };
    public static boolean valid(int index) { return index >= 0 && index < TITLES.length; }
}
