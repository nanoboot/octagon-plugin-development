ALTER TABLE "EPIC" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_EPIC_ALIAS" ON "EPIC"("ALIAS");

ALTER TABLE "STORY" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_STORY_ALIAS" ON "STORY"("ALIAS");

ALTER TABLE "DEV_TASK" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_DEV_TASK_ALIAS" ON "DEV_TASK"("ALIAS");

ALTER TABLE "DEV_SUB_TASK" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_DEV_SUB_TASK_ALIAS" ON "DEV_SUB_TASK"("ALIAS");

ALTER TABLE "BUG" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_BUG_ALIAS" ON "BUG"("ALIAS");

ALTER TABLE "ENHANCEMENT" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_ENHANCEMENT_ALIAS" ON "ENHANCEMENT"("ALIAS");

ALTER TABLE "INCIDENT" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_INCIDENT_ALIAS" ON "INCIDENT"("ALIAS");

ALTER TABLE "NEW_FEATURE" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_NEW_FEATURE_ALIAS" ON "NEW_FEATURE"("ALIAS");

ALTER TABLE "PROBLEM" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_PROBLEM_ALIAS" ON "PROBLEM"("ALIAS");

ALTER TABLE "PROPOSAL" ADD COLUMN "ALIAS" TEXT;
CREATE UNIQUE INDEX "UNIQUE_INDEX_PROPOSAL_ALIAS" ON "PROPOSAL"("ALIAS");
