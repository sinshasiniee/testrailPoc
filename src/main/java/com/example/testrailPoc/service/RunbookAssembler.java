package com.example.testrailPoc.service;

import com.example.testrailPoc.models.JiraRequirement;
import com.example.testrailPoc.models.MissingTestCase;
import com.example.testrailPoc.models.RequirementGap;
import com.example.testrailPoc.models.TestRailCase;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RunbookAssembler {
    public String assemble(List<RequirementGap> gaps, List<TestRailCase> unmappedCases) {
        StringBuilder sb = new StringBuilder();
        sb.append("# QA Runbook\n\n");
        sb.append("## SECTION 1: Gap Analysis & Action Table\n\n");
        sb.append("| Jira ID | Requirement Summary | Requirement Description | Identified Gap (Missing Test Case) | Risk Level | Action / Runbook Placement |\n");
        sb.append("| :--- | :--- | :--- | :--- | :--- | :--- |\n");

        if (gaps == null || gaps.isEmpty()) {
            sb.append("| - | - | - | No missing coverage identified | <span style=\"color:green;\">Low</span> | None |\n");
        } else {
            for (RequirementGap gap : gaps) {
                JiraRequirement requirement = gap.requirement();
                List<MissingTestCase> missingCases = gap.missingCases() == null ? List.of() : gap.missingCases();
                if (missingCases.isEmpty()) {
                    continue;
                }
                for (MissingTestCase missing : missingCases) {
                    sb.append("| ")
                            .append(requirement.issueKey()).append(" | ")
                            .append(safe(requirement.summary())).append(" | ")
                            .append(safe(requirement.description())).append(" | ")
                            .append("**[")
                            .append(safe(missing.type()))
                            .append("]** ")
                            .append(safe(missing.scenario())).append(" | ")
                            .append(riskHtml(missing.riskLevel())).append(" | ")
                            .append("Inject into the validation phase for ")
                            .append(safe(requirement.summary()))
                            .append(" |\n");
                }
            }
        }

        sb.append("\n## SECTION 2: Runbook Execution Sequence\n\n");
        if (gaps == null || gaps.isEmpty()) {
            sb.append("No execution gaps detected.\n");
        } else {
            int stepCounter = 1;
            for (RequirementGap gap : gaps) {
                JiraRequirement requirement = gap.requirement();
                sb.append("#### Module: ")
                        .append(safe(requirement.issueKey()))
                        .append(" - ")
                        .append(safe(requirement.summary()))
                        .append("\n");

                List<TestRailCase> existing = gap.existingCases() == null ? List.of() : gap.existingCases();
                int existingStep = 1;
                for (TestRailCase existingCase : existing) {
                    sb.append("- [x] Step ").append(stepCounter).append(".")
                            .append(existingStep)
                            .append(": Run ")
                            .append(existingCase.id()).append(" - ")
                            .append(safe(existingCase.title())).append("\n");
                    existingStep++;
                }

                List<MissingTestCase> missingCases = gap.missingCases() == null ? List.of() : gap.missingCases();
                int gapStep = 1;
                for (MissingTestCase missing : missingCases) {
                    sb.append("- [ ] Step ").append(stepCounter).append(".")
                            .append(existingStep + gapStep)
                            .append(": [GAP - PENDING TESTRAIL ID] ")
                            .append(safe(missing.scenario())).append("\n");
                    sb.append("    - *Context:* Identified via QA Gap Analysis for ").append(requirement.issueKey()).append("\n");
                    sb.append("    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm ")
                            .append(safe(missing.expectedResult())).append(".\n");
                    gapStep++;
                }
                stepCounter++;
            }
        }

        sb.append("\n## SECTION 3: Unmapped Test Cases\n\n");
        if (unmappedCases == null || unmappedCases.isEmpty()) {
            sb.append("No unmapped TestRail cases detected.\n");
        } else {
            for (TestRailCase unmapped : unmappedCases) {
                sb.append("- [").append(unmapped.id()).append("]: ")
                        .append(safe(unmapped.title())).append(" (Reason: Missing Reference Link)\n");
            }
        }

        return sb.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value.replace("|", "\\|").replace("\n", " ").trim();
    }

    private String riskHtml(String riskLevel) {
        String normalized = riskLevel == null || riskLevel.isBlank() ? "Medium" : riskLevel.trim();
        return switch (normalized.toLowerCase()) {
            case "high" -> "<span style=\"color:red;\">High</span>";
            case "medium" -> "<span style=\"color:orange;\">Medium</span>";
            default -> "<span style=\"color:green;\">Low</span>";
        };
    }
}
