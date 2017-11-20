<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%@ taglib uri="http://www.springframework.org/tags" prefix="sf"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <title>Job portal</title>

    <!-- Bootstrap -->
    <link href="../css/bootstrap.min.css" rel="stylesheet">
    <!-- for fontawesome icon css file -->
    <link href="../css/font-awesome.min.css" rel="stylesheet">
    <!-- superslides css -->
    <link rel="stylesheet" href="../css/superslides.css">
    <!-- for content animate css file -->
    <link rel="stylesheet" href="../css/animate.css">    
    <!-- slick slider css file -->
    <link href="../css/slick.css" rel="stylesheet">        
    <!-- website theme color file -->   
    <link id="switcher" href="../css/themes/cyan-theme.css" rel="stylesheet">   
    <!-- main site css file -->    
    <link href="../style.css" rel="stylesheet">
    <!-- google fonts  -->  
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>    
    <link href='http://fonts.googleapis.com/css?family=Raleway' rel='stylesheet' type='text/css'>
    <!-- Favicon -->
    <link rel="shortcut icon" href="../img/favicon.ico" type="image/x-icon">
  
  
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
<body>
  <!-- Preloader -->
  <div id="preloader">
    <div id="status">&nbsp;</div>
  </div>
 
  <!-- End Preloader -->   
  <a class="scrollToTop" href="#"><i class="fa fa-angle-up"></i></a>
  
  <!-- start navbar -->
  <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="../index.html">Job <span>Portal</span></a>
      </div>
      <div id="navbar" class="navbar-collapse collapse navbar_area">          
        <ul class="nav navbar-nav navbar-right custom_nav">
          <li class="active"><a href="#">Search</a></li>
           <li><a href="doGetJobSeeker.htm">Edit</a></li>
          <li><a href="doGetJobSeekerProfile.htm">Profile</a></li>
          <li><a href="doGetAppliedJob.htm">Applied</a></li>
          <li><a href="doLogout.htm">Logout</a></li>
        </ul>
      </div><!--/.nav-collapse -->
    </div>
  </nav>
  <br />
  <br />
  <br />
  <br />
 
  <!-- start image editing  -->
  <section id="blogArchive">
    <div class="container">
      <div class="row">
        <div class="col-lg-8 col-md-8 col-sm-12">
         <div class="blogArchive_area">
         <!-- start single archive post -->
          <div class="single_archiveblog">
            <div class="archiveblog_left">
              <img class="author_img" src="../img/blogger.jpg" alt="img">
              <h5 class="author_name">${job.employer.emailAddress}</h5>
            </div>

            <div class="archiveblog_right">
              <h2>${job.employer.name}</h2>
              <p>${job.description}</p>
              <ul>
                <li>${job.jobPosition}</li>
              </ul>
              <p>Company Description: ${job.employer.description}</p>
              <form action="doApplyForJob.htm" method="post">
              <input type="hidden" name="jobId" value="${job.id}">
              <input class="wpcf7-submit photo-submit" type="submit" value="Apply">
              </form>
            </div>
            <p style="color: red;">${result}</p>
          </div>
          <!-- End single archive post -->
          </div>
        </div>
        <div class="col-lg-4 col-md-4 col-sm-12">
         <div class="blog_sidebar">
         <!-- Start single side bar -->
         <div class="single_sidebar">
            <h2>Job Location</h2>
            <ul class="small_catg similar_nav">
             <c:forEach items="${job.locations}" var="location">
                <li>
                  <div class="media">
                    <div class="media-body">
                      <h4 class="media-heading"> ${location} </h4> 
                    </div>
                  </div>
                </li>  
               </c:forEach>                  
            </ul>
          </div>
  <!-- jQuery Library -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

  <!-- For content animatin  -->
  <script src="../js/wow.min.js"></script>
  <!-- bootstrap js file -->
  <script src="../js/bootstrap.min.js"></script> 

  <!-- superslides slider -->
  <script src="../js/jquery.easing.1.3.js"></script>
  <script src="../js/jquery.animate-enhanced.min.js"></script>
  <script src="../js/jquery.superslides.min.js" type="text/javascript" charset="utf-8"></script>
  <!-- slick slider js file -->
  <script src="../js/slick.min.js"></script>
  <!-- Google map -->
  <script src="https://maps.googleapis.com/maps/api/js"></script>
  <script src="../js/jquery.ui.map.js"></script>


  <!-- custom js file include -->
  <script src="../js/custom.js"></script>   
      
  </body>
</html>