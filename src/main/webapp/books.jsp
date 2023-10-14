<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./Assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for bootstrap template-->
    <link href="./Assets/css/sb-admin-2.min.css" rel="stylesheet">
    <title>Library - Books</title>
    <!-- Get Books -->
    <script>
        fetch("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/books").then(
            res => {
                res.json().then(
                    data => {
                        console.log(data);
                        if (data.length > 0) {

                            var temp = "";
                            data.forEach((itemData) => {
                                temp += "<tr>";
                                temp += "<td>" + itemData.BookTitle + "</td>";
                                temp += "<td>" + itemData.Author + "</td>";
                                temp += "<td>" + itemData.ISBN + "</td>";
                                temp += "<td>" + itemData.Pages + "</td>";
                                temp += "<td>" + itemData.Added + "</td>";
                                if(itemData.OnLoan === false) {
                                    temp += "<td>×</td></tr>";
                                } else {
                                    temp += "<td>✔</td></tr>";
                                }
                            });
                            document.getElementById('data').innerHTML = temp;
                            $('#dataTable').DataTable();
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
                <h1 class="h3 mb-2 text-gray-800">Books</h1>
                <p class="mb-4">This table lists all books stored in the library</p>
                <!-- DataTales -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Active Books</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>ISBN</th>
                                    <th>Pages</th>
                                    <th>Added On</th>
                                    <th>On Loan</th>
                                </tr>
                                </thead>
                                <tfoot>
                                <tr>
                                    <th>Title</th>
                                    <th>Author</th>
                                    <th>ISBN</th>
                                    <th>Pages</th>
                                    <th>Added On</th>
                                    <th>On Loan</th>
                                </tr>
                                </tfoot>
                                <tbody id="data">
                                </tbody>
                            </table>
                        </div>
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
