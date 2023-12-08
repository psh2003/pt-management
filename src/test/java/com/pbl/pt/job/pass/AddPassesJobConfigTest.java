package com.pbl.pt.job.pass;

import com.pbl.pt.config.TestBatchConfig;
import com.pbl.pt.repository.pass.BulkPassEntity;
import com.pbl.pt.repository.pass.BulkPassRepository;
import com.pbl.pt.repository.pass.BulkPassStatus;
import com.pbl.pt.repository.user.UserGroupMappingEntity;
import com.pbl.pt.repository.user.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AddPassesJobConfig.class, TestBatchConfig.class, AddPassesTasklet.class})
// 이용권 일괄 지급
public class AddPassesJobConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private BulkPassRepository bulkPassRepository;

    @Autowired
    private UserGroupMappingRepository userGroupMappingRepository;


    @Test
    public void test_addPassesJob() throws Exception {
        // given
        addBulkPassEntity();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        // then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("addPassesJob", jobInstance.getJobName());

    }

    private void addBulkPassEntity() {
        final LocalDateTime now = LocalDateTime.now();
        final String userGroupId = Double.valueOf(Math.random()).toString();
        final String userId = "1";

        BulkPassEntity bulkPassEntity = new BulkPassEntity();
        bulkPassEntity.setPackageSeq(1);
        bulkPassEntity.setBulkPassUserGroupId(userGroupId);
        bulkPassEntity.setStatus(BulkPassStatus.READY);
        bulkPassEntity.setCount(10);
        bulkPassEntity.setStartedAt(now);
        bulkPassEntity.setEndedAt(now.plusDays(60));

        bulkPassRepository.save(bulkPassEntity);

        UserGroupMappingEntity userGroupMappingEntity = new UserGroupMappingEntity();
        userGroupMappingEntity.setBulkPassUserGroupId(userGroupId);
        userGroupMappingEntity.setUserId(userId);
        userGroupMappingEntity.setUserGroupName("그룹");
        userGroupMappingEntity.setDescription("그룹 설명");

        userGroupMappingRepository.save(userGroupMappingEntity);

    }

}
