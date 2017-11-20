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
    <title>Job Portal</title>

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
  </head>
<body>
  <!-- =========================
    //////////////This Theme Design and Developed //////////////////////
    //////////// by www.wpfreeware.com======================--> 

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
 <section id="priceSection">
    <div class="container">
      <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12">
          <div class="client_title">
            <hr>
            <h2>Select <span>Suitable Job</span></h2>
            <div class="row">
			<div class="wow fadeInLeft">
            <form action="doSearchJob.htm"  class="submitphoto_form" method="post">
            	<input type="text" name="title" class="form-control wpcf7-text" placeholder="Job Title" value="${title}"><br/> 
            	Location: <input  type="checkbox" name="filter" value="LOCATION"> 
            	Position: <input type="checkbox" name="filter" value="POSITION">
            	<input class="wpcf7-submit photo-submit" type="submit" value="Filter">
            </form>
            </div>
            </div>
            </div>
          </div>
          <!-- Start Plan area -->
          <div class="pricearea">
            <ul class="price_nav wow bounceIn"> 
            <c:forEach items="${jobs}" var="job">              
              <li>
                <h2 class="price_heading">${job.title}</h2>
               <ul class="pfeatured_nav">
                  <li><strong> Position: </strong> ${job.jobPosition} </li>
                  <li><strong> Company: </strong> ${job.employer.name} </li>
                  <li>${job.employer.emailAddress}</li>
                </ul>
                <a class="get_button" href="doGetJob.htm?jobid=${job.id}">Check Job</a>
              </li>
              <!-- Start single Plan -->
             </c:forEach>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </section>
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