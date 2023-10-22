<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./Assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for bootstrap template-->
    <link href="./Assets/css/sb-admin-2.min.css" rel="stylesheet">
    <title>Library - Loan a book</title>
    <!-- Get Books -->
    <script>
        fetch("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/users").then(
            res => {
                res.json().then(
                    data => {
                        if (data.length > 0) {
                            var temp = "<option disabled selected>Select a user</option>";
                            data.forEach((itemData) => {
                                temp += "<option value='"+itemData.Id+"'>" + itemData.Name + " - (" + itemData.Email + ")</option>";
                            });
                            document.getElementById('users').innerHTML = temp;
                        }
                    }
                )
            }
        )
        fetch("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/books/available").then(
            res => {
                res.json().then(
                    data => {
                        if (data.length > 0) {
                            var temp = "<option disabled selected>Select a book</option>";
                            data.forEach((itemData) => {
                                temp += "<option value='"+itemData.Id+"'>" + itemData.BookTitle + " - " + itemData.Author + "</option>";
                            });
                            document.getElementById('books').innerHTML = temp;
                        }
                    }
                )
            }
        )
    </script>
</head>
<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">
    <jsp:include page="Shared Components/headersidebar.html" />
            <!-- Begin Page Content -->
            <div class="container-fluid">
                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Loan a Book</h1>
                <p class="mb-4">Fill out the form to create a new loan</p>
                <!-- DataTales -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">New Loan</h6>
                    </div>
                    <div class="card-body">
                        <form action="/MongoDB-Library-1.0-SNAPSHOT/loans" method="post">
                            <div class="form-group">
                                <select required name="users" id="users">
                                </select>
                            </div>
                            <div class="form-group">
                                <select required name="books" id="books">
                                </select>
                            </div>
                            <button type="submit" style="width: 25%" class="btn btn-primary btn-block">
                                Loan Book
                            </button>
                        </form>
                    </div>
                </div>

            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- End of Main Content -->
    </div>
    <!-- End of Content Wrapper -->
</div>
<!-- End of Page Wrapper -->

<!-- Bootstrap core JavaScript-->
<script src="./Assets/vendor/jquery/jquery.min.js"></script>
<script src="./Assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="./Assets/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="./Assets/js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="./Assets/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="./Assets/vendor/datatables/dataTables.bootstrap4.min.js"></script>
</body>
</html>
