package Prochat_project.fixture;

import lombok.Data;

public class TestInfoFixture {
    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setPostId(1L);
        info.setUserId(1L);
        info.setUserName("name");
        info.setPassword("password");
        info.setTitle("title");
        info.setContent("content");
        return info;
    }

    @Data
    public static class TestInfo {
        private Long postId;
        private Long userId;
        private String userName;
        private String password;
        private String title;
        private String content;
    }
}
