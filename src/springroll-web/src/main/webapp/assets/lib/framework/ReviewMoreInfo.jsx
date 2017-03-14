import React from 'react'
import Application from 'App.js';
var moment = require('moment');
const ReviewMoreInfo = ({alert}) => {
    return (
        <span>
            {
                alert.reviewLog != null && alert.reviewLog.length > 0 &&
                <span>
                    <h4>{Application.Localize("ui.review.previousReviewers")}</h4>
                    <table className="table table-hover table-striped">
                        <thead>
                        <tr>
                            <th>{Application.Localize("ui.review.reviewer")}</th>
                            <th>{Application.Localize("ui.review.comments")}</th>
                            <th>{Application.Localize("ui.review.time")}</th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            alert.reviewLog.map((review, index) =>
                                (<tr key={index}>
                                    <td>{review.reviewer}</td>
                                    <td>{review.reviewComment}</td>
                                    <td>{moment(review.time).format(Application.getMomentFormatForDateTime())}</td>
                                </tr>))
                        }
                        </tbody>
                    </table>
                </span>
            }
            <table className="table table-hover table-striped">
                <thead>
                <tr>
                    <th>{Application.Localize("ui.review.breachedRuleName")}</th>
                    <th>{Application.Localize("ui.review.breachedRuleText")}</th>
                </tr>
                </thead>
                <tbody>
                {
                    alert.businessValidationResult.map((violation, index) =>
                        (<tr key={index}>
                            <td>{Application.Localize(violation.violatedRule)}</td>
                            <td>{Application.Localize(violation.messageKey, violation.args)}</td>
                        </tr>))
                }
                </tbody>
            </table>
        </span>
    );
};

export  default ReviewMoreInfo;