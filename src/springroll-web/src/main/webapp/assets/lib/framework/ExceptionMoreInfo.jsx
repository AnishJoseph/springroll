import React from 'react'
import Application from 'App.js';


const ExceptionMoreInfo = ({alert}) => {
    return (
    <span>
        <h4 className="text-info">{Application.Localize('Exception Summary')}</h4>
        <table className="table table-striped table-bordered table-hover"><tbody>
            <tr><td>{Application.Localize('Service Name')}</td><td>{alert.serviceMessageArgs[0]}</td></tr>
            <tr><td>{Application.Localize('1st event in this Transaction')}</td><td>{alert.serviceEventName}</td></tr>
            <tr><td>{Application.Localize('Exception Event Context')}</td><td>{alert.eventThatCausedException}</td></tr>
            <tr><td>{Application.Localize('Job ID')}</td><td>{alert.jobId}</td></tr>
            <tr><td>{Application.Localize('Transaction Leg ID')}</td><td>{alert.transactionLegId}</td></tr>
        </tbody></table>
        <h4 className="text-info">{Application.Localize('Stack Summary')}</h4>

        <table className="table table-striped table-bordered">
            <thead><tr>
                <th>Message</th>
                <th>Exception</th>
                <th>File</th>
                <th>Method</th>
                <th>Line</th>
            </tr></thead>
            <tbody>
            {
                alert.springrollExceptionDebugInfoList.map((debugInfo, index) => {
                return (
                    <tr key={index}>
                        <td>{debugInfo.message}</td>
                        <td>{debugInfo.exception}</td>
                        <td>{debugInfo.fileName}</td>
                        <td>{debugInfo.methodName}</td>
                        <td>{debugInfo.lineNumber}</td>
                    </tr>
                )})
            }
            </tbody>
        </table>
    </span>
    );
};
export default ExceptionMoreInfo;
