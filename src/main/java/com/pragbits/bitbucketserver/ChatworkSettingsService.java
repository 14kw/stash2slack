package com.pragbits.bitbucketserver;

import com.atlassian.bitbucket.repository.Repository;
import javax.annotation.Nonnull;

public interface ChatworkSettingsService {

    @Nonnull
    ChatworkSettings getChatworkSettings(@Nonnull Repository repository);

    @Nonnull
    ChatworkSettings setChatworkSettings(@Nonnull Repository repository, @Nonnull ChatworkSettings settings);

}
