package mib;

import java.io.Serializable;

public class Permission implements Serializable {
    private final String readCommunityString;
    private final String  writeCommunityString;

    public Permission(String readCommunityStrings, String writeCommunityString) {
        this.readCommunityString = readCommunityStrings;
        this.writeCommunityString = writeCommunityString;
    }

    public boolean hasReadAccess(String communityString) {
        return readCommunityString.equals(communityString) || writeCommunityString.equals(communityString);
    }

    public boolean hasWriteAccess(String communityString) {
        return writeCommunityString.equals(communityString);
    }
}
