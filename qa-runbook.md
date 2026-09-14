# QA Runbook

## SECTION 1: Gap Analysis & Action Table

| Jira ID | Requirement Summary | Requirement Description | Identified Gap (Missing Test Case) | Risk Level | Action / Runbook Placement |
| :--- | :--- | :--- | :--- | :--- | :--- |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[Edge]** Login with empty username field | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[Edge]** Login with empty password field | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[Edge]** Login with special characters in username | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[Edge]** Login with special characters in password | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[Edge]** Login with too short username | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[Edge]** Login with too short password | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ001 | User Login | The system must allow users to log in with valid credentials (username + password) | **[ErrorHandling]** Login with unauthorized attempts | <span style="color:red;">High</span> | Inject into the validation phase for User Login |
| RQ002 | Invalid Login | The system must reject login attempts with invalid credentials and show an error message | **[ErrorHandling]** Login with valid username and incorrect password | <span style="color:red;">High</span> | Inject into the validation phase for Invalid Login |
| RQ002 | Invalid Login | The system must reject login attempts with invalid credentials and show an error message | **[ErrorHandling]** Login with incorrect username and valid password | <span style="color:red;">High</span> | Inject into the validation phase for Invalid Login |
| RQ002 | Invalid Login | The system must reject login attempts with invalid credentials and show an error message | **[ErrorHandling]** Login with both invalid username and password | <span style="color:red;">High</span> | Inject into the validation phase for Invalid Login |
| RQ002 | Invalid Login | The system must reject login attempts with invalid credentials and show an error message | **[ErrorHandling]** Login with empty username and valid password | <span style="color:red;">High</span> | Inject into the validation phase for Invalid Login |
| RQ002 | Invalid Login | The system must reject login attempts with invalid credentials and show an error message | **[ErrorHandling]** Login with valid username and empty password | <span style="color:red;">High</span> | Inject into the validation phase for Invalid Login |
| RQ003 | Password Reset | Users must be able to reset their password via email. | **[ErrorHandling]** Request Password Reset with an invalid email format | <span style="color:red;">High</span> | Inject into the validation phase for Password Reset |
| RQ003 | Password Reset | Users must be able to reset their password via email. | **[ErrorHandling]** Request Password Reset with an unregistered email | <span style="color:red;">High</span> | Inject into the validation phase for Password Reset |
| RQ003 | Password Reset | Users must be able to reset their password via email. | **[ErrorHandling]** Request Password Reset with an email that is already being reset | <span style="color:orange;">Medium</span> | Inject into the validation phase for Password Reset |
| RQ003 | Password Reset | Users must be able to reset their password via email. | **[Performance]** Password Reset request rate limit test | <span style="color:orange;">Medium</span> | Inject into the validation phase for Password Reset |
| RQ003 | Password Reset | Users must be able to reset their password via email. | **[Boundary]** Password Reset request with an extremely long email | <span style="color:green;">Low</span> | Inject into the validation phase for Password Reset |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[Edge]** Dashboard Access - Empty Credentials | <span style="color:red;">High</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[Edge]** Dashboard Access - Incorrect Password | <span style="color:red;">High</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[Edge]** Dashboard Access - Expired Session | <span style="color:red;">High</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[Edge]** Dashboard Access - Inactive User | <span style="color:red;">High</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[Boundary]** Dashboard Access - Long Username | <span style="color:orange;">Medium</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[Boundary]** Dashboard Access - Long Password | <span style="color:orange;">Medium</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[ErrorHandling]** Dashboard Access - Invalid Character in Username | <span style="color:orange;">Medium</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ004 | Dashboard Access	After login | users must be redirected to the dashboard page. | **[ErrorHandling]** Dashboard Access - Invalid Character in Password | <span style="color:orange;">Medium</span> | Inject into the validation phase for Dashboard Access	After login |
| RQ005 | Session Timeout | User sessions must expire after 15 minutes of inactivity. | **[Edge]** Session inactivity duration exceeds 15 minutes | <span style="color:red;">High</span> | Inject into the validation phase for Session Timeout |
| RQ005 | Session Timeout | User sessions must expire after 15 minutes of inactivity. | **[Edge]** Session inactivity duration is less than 15 minutes | <span style="color:orange;">Medium</span> | Inject into the validation phase for Session Timeout |
| RQ005 | Session Timeout | User sessions must expire after 15 minutes of inactivity. | **[ErrorHandling]** User attempts to manipulate session timeout | <span style="color:red;">High</span> | Inject into the validation phase for Session Timeout |
| RQ005 | Session Timeout | User sessions must expire after 15 minutes of inactivity. | **[Edge]** User attempts to log in during session timeout | <span style="color:orange;">Medium</span> | Inject into the validation phase for Session Timeout |

## SECTION 2: Runbook Execution Sequence

#### Module: RQ001 - User Login
- [x] Step 1.1: Run 55 - Successful Login with Valid Credentials
- [x] Step 1.2: Run 56 - Login Failure with Incorrect Password
- [ ] Step 1.4: [GAP - PENDING TESTRAIL ID] Login with empty username field
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Username cannot be empty'.
- [ ] Step 1.5: [GAP - PENDING TESTRAIL ID] Login with empty password field
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Password cannot be empty'.
- [ ] Step 1.6: [GAP - PENDING TESTRAIL ID] Login with special characters in username
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid username format'.
- [ ] Step 1.7: [GAP - PENDING TESTRAIL ID] Login with special characters in password
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid password format'.
- [ ] Step 1.8: [GAP - PENDING TESTRAIL ID] Login with too short username
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Username must be at least 3 characters long'.
- [ ] Step 1.9: [GAP - PENDING TESTRAIL ID] Login with too short password
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Password must be at least 8 characters long'.
- [ ] Step 1.10: [GAP - PENDING TESTRAIL ID] Login with unauthorized attempts
    - *Context:* Identified via QA Gap Analysis for RQ001
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 403 and the message 'Access denied, too many failed login attempts'.
#### Module: RQ002 - Invalid Login
- [ ] Step 2.2: [GAP - PENDING TESTRAIL ID] Login with valid username and incorrect password
    - *Context:* Identified via QA Gap Analysis for RQ002
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid password'.
- [ ] Step 2.3: [GAP - PENDING TESTRAIL ID] Login with incorrect username and valid password
    - *Context:* Identified via QA Gap Analysis for RQ002
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid username'.
- [ ] Step 2.4: [GAP - PENDING TESTRAIL ID] Login with both invalid username and password
    - *Context:* Identified via QA Gap Analysis for RQ002
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid input'.
- [ ] Step 2.5: [GAP - PENDING TESTRAIL ID] Login with empty username and valid password
    - *Context:* Identified via QA Gap Analysis for RQ002
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Username cannot be empty'.
- [ ] Step 2.6: [GAP - PENDING TESTRAIL ID] Login with valid username and empty password
    - *Context:* Identified via QA Gap Analysis for RQ002
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Password cannot be empty'.
#### Module: RQ003 - Password Reset
- [x] Step 3.1: Run 57 - Request Password Reset with Registered Email
- [ ] Step 3.3: [GAP - PENDING TESTRAIL ID] Request Password Reset with an invalid email format
    - *Context:* Identified via QA Gap Analysis for RQ003
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid email format'.
- [ ] Step 3.4: [GAP - PENDING TESTRAIL ID] Request Password Reset with an unregistered email
    - *Context:* Identified via QA Gap Analysis for RQ003
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 404 and the message 'Email not registered'.
- [ ] Step 3.5: [GAP - PENDING TESTRAIL ID] Request Password Reset with an email that is already being reset
    - *Context:* Identified via QA Gap Analysis for RQ003
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 409 and the message 'Password reset already in progress for this email'.
- [ ] Step 3.6: [GAP - PENDING TESTRAIL ID] Password Reset request rate limit test
    - *Context:* Identified via QA Gap Analysis for RQ003
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system should limit the number of password reset requests per hour.
- [ ] Step 3.7: [GAP - PENDING TESTRAIL ID] Password Reset request with an extremely long email
    - *Context:* Identified via QA Gap Analysis for RQ003
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system should reject the request with HTTP 400 and the message 'Email is too long'.
#### Module: RQ004 - Dashboard Access	After login
- [ ] Step 4.2: [GAP - PENDING TESTRAIL ID] Dashboard Access - Empty Credentials
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 401 and the message 'Unauthorized' or 'Invalid credentials'.
- [ ] Step 4.3: [GAP - PENDING TESTRAIL ID] Dashboard Access - Incorrect Password
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 401 and the message 'Unauthorized' or 'Invalid credentials'.
- [ ] Step 4.4: [GAP - PENDING TESTRAIL ID] Dashboard Access - Expired Session
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system redirects to the login page.
- [ ] Step 4.5: [GAP - PENDING TESTRAIL ID] Dashboard Access - Inactive User
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system redirects to the login page.
- [ ] Step 4.6: [GAP - PENDING TESTRAIL ID] Dashboard Access - Long Username
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system accepts the request and redirects to the dashboard page.
- [ ] Step 4.7: [GAP - PENDING TESTRAIL ID] Dashboard Access - Long Password
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system accepts the request and redirects to the dashboard page.
- [ ] Step 4.8: [GAP - PENDING TESTRAIL ID] Dashboard Access - Invalid Character in Username
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid input' or 'Invalid character detected'.
- [ ] Step 4.9: [GAP - PENDING TESTRAIL ID] Dashboard Access - Invalid Character in Password
    - *Context:* Identified via QA Gap Analysis for RQ004
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Invalid input' or 'Invalid character detected'.
#### Module: RQ005 - Session Timeout
- [ ] Step 5.2: [GAP - PENDING TESTRAIL ID] Session inactivity duration exceeds 15 minutes
    - *Context:* Identified via QA Gap Analysis for RQ005
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 400 and the message 'Session has expired due to inactivity'.
- [ ] Step 5.3: [GAP - PENDING TESTRAIL ID] Session inactivity duration is less than 15 minutes
    - *Context:* Identified via QA Gap Analysis for RQ005
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system does not reject the request and the user session remains active.
- [ ] Step 5.4: [GAP - PENDING TESTRAIL ID] User attempts to manipulate session timeout
    - *Context:* Identified via QA Gap Analysis for RQ005
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system rejects the request with HTTP 403 and the message 'Forbidden: Session timeout manipulation is not allowed'.
- [ ] Step 5.5: [GAP - PENDING TESTRAIL ID] User attempts to log in during session timeout
    - *Context:* Identified via QA Gap Analysis for RQ005
    - *Temporary Manual Action:* Validate when the execution thread reaches the verification stage and confirm The system creates a new session and logs in the user.

## SECTION 3: Unmapped Test Cases

- [58]: Successful Post-Login Redirection (Reason: Missing Reference Link)
- [59]: Session Persistence During Active Use (Reason: Missing Reference Link)
