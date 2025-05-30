package kr.ac.yuhan.cs.androidproject.dto;

public class GroupSummary {
    private long id;
    private String groupName;

    public GroupSummary(long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public long getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }
}
