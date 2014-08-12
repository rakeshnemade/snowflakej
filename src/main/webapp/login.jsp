<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <link rel='stylesheet', href='media/css/bootstrap.min.css'></link>
    <script src='media/js/jquery.min.js'></script>
    <script src='media/js/bootstrap.min.js'></script>
    <style>
        .colorgraph {
          height: 5px;
          border-top: 0;
          background: #c4e17f;
          border-radius: 5px;
          background-image: -webkit-linear-gradient(left, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%, #f7fdca 25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%, #db9dbe 50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%, #669ae1 87.5%, #62c2e4 87.5%, #62c2e4);
          background-image: -moz-linear-gradient(left, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%, #f7fdca 25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%, #db9dbe 50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%, #669ae1 87.5%, #62c2e4 87.5%, #62c2e4);
          background-image: -o-linear-gradient(left, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%, #f7fdca 25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%, #db9dbe 50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%, #669ae1 87.5%, #62c2e4 87.5%, #62c2e4);
          background-image: linear-gradient(to right, #c4e17f, #c4e17f 12.5%, #f7fdca 12.5%, #f7fdca 25%, #fecf71 25%, #fecf71 37.5%, #f0776c 37.5%, #f0776c 50%, #db9dbe 50%, #db9dbe 62.5%, #c49cde 62.5%, #c49cde 75%, #669ae1 75%, #669ae1 87.5%, #62c2e4 87.5%, #62c2e4);
        }
    </style>
</head>
      <body style="background:#212121">
        <nav class="navbar navbar-default" role="navigation">
          <div class="container">
            <div class="navbar-header">
              <a class="navbar-brand" href="#"><img  src="/favicon.ico"></img><span> User Management</span></a>
            </div>
          </div>
        </nav>

        <div class="container" >
          <div class="row" style="margin-top:20px;">
                    <div class="col-xs-5">
                        <!-- Nav tabs -->
                        <ul class="nav nav-tabs">
                          <li class="active"><a href="#signin_tab" data-toggle="tab">Sign In</a></li>
                          <li><a href="#signup_tab" data-toggle="tab" class="hide">Sign Up</a></li>
                        </ul>

                        <!-- Tab panes -->
                        <div class="tab-content">
                          <div class="tab-pane active" id="signin_tab">
                            <div class="row">
                                <div class="col-xs-12">
                                    <form role="form" name="loginForm" action="j_spring_security_check" method="post">
                                        <fieldset>
                                            <h2 style="color:whitesmoke">Please Sign In</h2>
                                            <hr class="colorgraph">
                                            <h4 style="color:red"><c:out value="${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}" /></h4>
                                            <div class="form-group">
                                                <input type="text" name="j_username" id="uname" class="form-control input-lg" placeholder="User Name">
                                            </div>
                                            <div class="form-group">
                                                <input type="password" name="j_password" id="password" class="form-control input-lg" placeholder="Password">
                                            </div>
                                            <span class="button-checkbox">
                                                <label style="color:whitesmoke">Remember Me</label>
                                                <input type="checkbox" name="_spring_security_remember_me" id="_spring_security_remember_me">
                                                <a href="" class="btn btn-link pull-right">Forgot Password?</a>
                                            </span>
                                            <hr class="colorgraph">
                                            <div class="row">
                                                <div class="col-xs-6 col-sm-6 col-md-6">
                                                    <input type="submit" class="btn btn-lg btn-success btn-block" value="Sign In">
                                                </div>
                                                <div class="col-xs-6 col-sm-6 col-md-6">
                                                    <a href="" class="btn btn-lg btn-primary btn-block hide">Register</a>
                                                </div>
                                            </div>
                                        </fieldset>
                                    </form>
                                </div>
                            </div>
                          </div>
                          <div class="tab-pane" id="signup_tab">
                            <div class="row">
                                <div class="col-xs-12">
                                    <form role="form">
                                        <h2>Please Sign Up <small>Its free</small></h2>
                                        <hr class="colorgraph">
                                            <div class="form-group">
                                                <input type="text" name="display_name" id="display_name" class="form-control input-lg" placeholder="Display Name" tabindex="3">
                                            </div>
                                        <div class="form-group">
                                            <input type="email" name="email" id="email" class="form-control input-lg" placeholder="Email Address" tabindex="4">
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-12 col-sm-6 col-md-6">
                                                <div class="form-group">
                                                    <input type="password" name="password" id="password" class="form-control input-lg" placeholder="Password" tabindex="5">
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-6 col-md-6">
                                                <div class="form-group">
                                                    <input type="password" name="password_confirmation" id="password_confirmation" class="form-control input-lg" placeholder="Confirm Password" tabindex="6">
                                                </div>
                                            </div>
                                        </div>
                                        <hr class="colorgraph">
                                        <div class="row">
                                            <div class="col-xs-12 col-md-6"><input type="submit" value="Register" class="btn btn-primary btn-block btn-lg" tabindex="7"></div>
                                            <div class="col-xs-12 col-md-6"><a href="#" class="btn btn-success btn-block btn-lg">Sign In</a></div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                          </div>
                        </div>
                    </div>   
                    <div class="col-xs-7 text-center" >
                        <img src="media/images/PRETTYGRAPHSM.png" height="375px" style="padiing:20px">

                    </div>    
                     
                </div>    

                <div class="row">
                    
                </div>
        </div>    

      </body>

      </html>