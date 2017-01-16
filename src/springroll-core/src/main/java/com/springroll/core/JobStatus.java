package com.springroll.core;

/**
 * Created by anishjoseph on 16/01/17.
 */
public enum JobStatus {
    InProgress,
    Success,
    FailedInRootTransaction,
    FailedInSecondaryTransaction,
    UnderReview,
    RejectedAndForwarded,
    RejectedAndDiscarded,
}
