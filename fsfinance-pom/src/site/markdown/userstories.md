# User stories

## Open

### US0002: Login/Logout to the service

**Description**

I can login to the service using a previously create user account. If I am logged in I can logout again.

**Acceptance criteria**

  * Login with valid user credentials is possible
  * Login with invalid/non-existing user credentials is not possible
  * Logout is working and terminates a user's session
  * Restricted pages can only be accessed if a user is logged in

### US0003: Delete ones own user account

**Description**

If I am logged in I can delete my user account. After successful account deletion I am logged out.

**Acceptance criteria**

  * Current user passwords needs to be entered before the user's account is deleted actually
  * Deletion of a user account also deletes all other data belonging to this user
  * Logging into the service with credentials from a previously deleted user is not possible

### USXXXX: Update/Modify my user profile

### USXXXX: (Admin) User account management

## Closed

### <del>US0001: Register a new user account</del>

**Description**

I want to be able to sign up to the service by creating a new user account.

**Acceptance criteria**

  * After successfull registration the user's credentials are persisted in the database
  * Registration of an already existing user is not possible