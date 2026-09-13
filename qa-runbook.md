### SECTION 1: The Gap Analysis & Action Table

| Jira ID | Requirement Summary | Requirement Description | Identified Gap (Missing Test Case) | Risk Level | Action / Runbook Placement |
|---------|---------------------|-------------------------|-----------------------------------|------------|---------------------------|
| RQ001    | User Login          | The system must allow users to log in with valid credentials (username + password) | [Negative] Login Failure with Incorrect Username | High | Module 1: Authentication |
| RQ001    | User Login          | The system must allow users to log in with valid credentials (username + password) | [Edge] Successful Login with Empty Password | High | Module 1: Authentication |
| RQ002    | Invalid Login       | The system must reject login attempts with invalid credentials and show an error message | [Negative] Login Success with Invalid Credentials | High | Module 1: Authentication |
| RQ003    | Password Reset      | Users must be able to reset their password via email. | [Positive] Successful Password Reset | Medium | Module 3: Account Management |
| RQ004    | Dashboard Access     | users must be redirected to the dashboard page. after login | [Edge] Redirection to Dashboard with Invalid Credentials | Medium | Module 1: Authentication |
| RQ005    | Session Timeout      | User sessions must expire after 15 minutes of inactivity. | [Performance] Session Timeout after 16 minutes of inactivity | Medium | Module 2: Session Management |

### SECTION 2: Runbook Execution Sequence (The Runbook Insertion Format)

#### Module 1: Authentication
- [x] Step 1.1: Run 7 - Authentication
  - *Context:* Verifies the successful login with valid credentials.

- [ ] Step 1.2: [GAP - PENDING TESTRAIL ID] Login Failure with Incorrect Username
  - *Context:* Identified via LLM Gap Analysis for RQ001.
  - *Temporary Manual Action:* Enter an incorrect username and observe the error message.

- [ ] Step 1.3: [GAP - PENDING TESTRAIL ID] Successful Login with Empty Password
  - *Context:* Identified via LLM Gap Analysis for RQ001.
  - *Temporary Manual Action:* Enter a valid username and leave the password field empty.

- [x] Step 1.4: Run 56 - Login Failure with Incorrect Password
  - *Context:* Verifies the rejection of login attempts with invalid credentials.

- [ ] Step 1.5: [GAP - PENDING TESTRAIL ID] Login Success with Invalid Credentials
  - *Context:* Identified via LLM Gap Analysis for RQ002.
  - *Temporary Manual Action:* Enter invalid credentials and observe if the login is successful.

#### Module 2: Session Management
- [x] Step 2.1: Run 58 - Successful Post-Login Redirection
  - *Context:* Verifies successful redirection to the dashboard page after a successful login.

- [ ] Step 2.2: [GAP - PENDING TESTRAIL ID] Redirection to Dashboard with Invalid Credentials
  - *Context:* Identified via LLM Gap Analysis for RQ004.
  - *Temporary Manual Action:* Enter invalid credentials and observe the redirection.

#### Module 3: Account Management
- [x] Step 3.1: Run 57 - Request Password Reset with Registered Email
  - *Context:* Verifies the successful password reset with a registered email.

### SECTION 3: Unmapped Test Cases
- [59]: Session Persistence During Active Use (Reason: Missing Reference Link)
- [60]: Session Timeout after 17 minutes of inactivity (Reason: Missing Reference Link)