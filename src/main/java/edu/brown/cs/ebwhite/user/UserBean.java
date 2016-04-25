package edu.brown.cs.ebwhite.user;

import java.util.Set;

public class UserBean implements User {

  /**
   * Node ID.
   */
  private int mID;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private int phone;
  private Set<Integer> friends;

  public UserBean(int id, Set<Integer> friendSet, String uname, String fname,
      String lname, String mail, int pho) {
    this.mID = id;
    this.friends = friendSet;
    this.username = uname;
    this.firstName = fname;
    this.lastName = lname;
    this.email = mail;
    this.phone = pho;
  }

  @Override
  public int getId() {
    return mID;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public int getPhone() {
    return phone;
  }

  @Override
  public Set<Integer> getFriends() {
    return friends;
  }


}
