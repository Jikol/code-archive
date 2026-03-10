USE [JAV0032]
GO

/****** Object:  StoredProcedure [ETL].[SetExecutionStatus]    Script Date: 3/28/2025 9:44:26 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


ALTER PROCEDURE [ETL].[SetExecutionStatus]
    @ExecutionId INT,
    @BatchID INT,
    @Status NVARCHAR(1),
    @SourceRowCount INT,
    @DestRowCount INT,
    @Inserted INT,
    @Updated INT,
    @Deleted INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @CURRENT_TIME DATETIME = GETDATE();



    -- Update the execution record with the provided parameters
    UPDATE [ETL].[Executions]
    SET 
        [Status] = @Status,
        [SourceRowCount] = @SourceRowCount,
        [DestRowCount] = @DestRowCount,
        [Inserted] = @Inserted,
        [Updated] = @Updated,
        [Deleted] = @Deleted,
        [Ended] = CASE WHEN @Status = 'C' THEN @CURRENT_TIME ELSE [Ended] END
    WHERE 
        [ExecutionId] = @ExecutionId
        AND [BatchID] = @BatchID;

    -- Return the status
    SELECT @Status AS Status;
END;
GO

