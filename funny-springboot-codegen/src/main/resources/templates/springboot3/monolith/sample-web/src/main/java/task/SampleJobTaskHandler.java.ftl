
package ${package}.task;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SampleJobTaskHandler {

    @XxlJob("sampleJobHandler")
    public void sampleJobHandler() {
        try {

            XxlJobHelper.handleSuccess("sampleJobHandler success");
            XxlJobHelper.handleFail("sampleJobHandler fail");
        } catch (Exception e) {
            log.error("sampleJobHandler error,message:{}",e.getMessage(),e);
        }
    }


}
