package opaweb.authz

default allow = false

allow {
  input.method == "GET"
  input.path = ["hello"]
  is_user
}

allow {
  input.method == "GET"
  input.path = ["bye"]
  is_admin
}


# user is allowed if he has a user role
is_user {

	# for some `i`...
	some i

  input.roles[i].authority == "ROLE_USER"
}

# user is allowed if he has a admin role
is_admin {

	# for some `i`...
	some i

  input.roles[i].authority == "ROLE_ADMIN"
}