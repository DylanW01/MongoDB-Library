<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./Assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for bootstrap template-->
    <link href="./Assets/css/sb-admin-2.min.css" rel="stylesheet">
    <title>Library - Fines</title>
    <script>
        fetch("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/fines").then(
            res => {
                res.json().then(
                    data => {
                        console.log(data);
                        if (data.length > 0) {
                            var temp = "";
                            data.forEach((itemData) => {
                                temp += "<tr>";
                                temp += "<td>" + itemData.id + "</td>";
                                temp += "<td>" + itemData.fine_amount + "</td>";
                                temp += "<td>" + itemData.fine_date + "</td>";
                                temp += "<td>" + itemData.paid + "</td>";
                                temp += "<td>" + itemData.bookData.Title + "</td>";
                                temp += "<td>" + itemData.bookData.Author + "</td>";
                                temp += "<td>" + itemData.loanData.return_by + "</td>";
                                temp += "<td>" + itemData.loanData.return_date + "</td>";
                                temp += "<td>" + itemData.userData.name + "</td>";

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
                <h1 class="h3 mb-2 text-gray-800">Fines</h1>
                <p class="mb-4">This table lists all issued fines for customers</p>

                <!-- DataTales Example -->
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold text-primary">Fines</h6>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>Fine ID</th>
                                    <th>Cost</th>
                                    <th>Issue Date</th>
                                    <th>Paid?</th>
                                    <th>Book Title</th>
                                    <th>Book Author</th>
                                    <th>Loan Due</th>
                                    <th>Book Returned On</th>
                                    <th>Customer Name</th>
                                </tr>
                                </thead>
                                <tfoot>
                                <tr>
                                    <th>Fine ID</th>
                                    <th>Cost</th>
                                    <th>Issue Date</th>
                                    <th>Paid?</th>
                                    <th>Book Title</th>
                                    <th>Book Author</th>
                                    <th>Loan Due</th>
                                    <th>Book Returned On</th>
                                    <th>Customer Name</th>
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

        <!-- Footer -->
<jsp:include page="Shared Components/footer.html" />

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

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

<!-- Page level custom scripts -->
<script src="./Assets/js/demo/datatables-demo.js"></script>
</body>
</html>
