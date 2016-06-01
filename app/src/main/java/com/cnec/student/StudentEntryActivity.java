package com.cnec.student;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StudentEntryActivity extends AppCompatActivity {

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_entry);

        if ( this.getIntent().hasExtra("ID") ) {
            this.id = this.getIntent().
                    getIntExtra("ID", -1);

            this.showFields( id );
        }

        Button btnSave = (Button) findViewById(R.id.save);
        Button btnDelete = (Button) findViewById(R.id.delete);

        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idField = (EditText) findViewById(R.id.idField);
                EditText code = (EditText) findViewById(R.id.code);
                EditText name = (EditText) findViewById(R.id.name);
                EditText email = (EditText) findViewById(R.id.email);

                if (! isFieldEmpty(code, name, email)){
                    saveRecord(code, name, email, idField);
                    finish();
                }
            }

            private void saveRecord(EditText code, EditText name,
                                    EditText email, EditText idField) {
                if (TextUtils.isEmpty(idField.getText())){
                    StudentActivity.getStudentDB().
                            insertStudent(code.getText().toString(),
                                    name.getText().toString(),
                                    email.getText().toString());
                } else {
                    StudentActivity.getStudentDB().
                            updateStudent(
                                    idField.getText().toString(),
                                    code.getText().toString(),
                                    name.getText().toString(),
                                    email.getText().toString());
                }
            }

            private boolean isFieldEmpty(EditText ...fields) {
                boolean empty = false;

                for (EditText field: fields){
                    if (TextUtils.isEmpty(field.getText().toString())){
                        empty = true;
                        field.setError("Campo deve ser preenchido!");
                    }
                }

                return empty;
            }
        });

        btnDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idField = (EditText) findViewById(R.id.idField);
                EditText code = (EditText) findViewById(R.id.code);
                EditText name = (EditText) findViewById(R.id.name);
                EditText email = (EditText) findViewById(R.id.email);


                    deleteRecord(idField);
                    finish();

            }
            private void deleteRecord(EditText idField) {
                    StudentActivity.getStudentDB().deleteStudent(idField.getText().toString());

            }
        });

    }

    private void showFields(int id) {
        Cursor student = StudentActivity.getStudentDB().getStudent(id);

        EditText idField = (EditText) findViewById(R.id.idField);
        EditText code = (EditText) findViewById(R.id.code);
        EditText name = (EditText) findViewById(R.id.name);
        EditText email = (EditText) findViewById(R.id.email);

        idField.setText(new Integer(id).toString());
        code.setText(student.getString(student.getColumnIndex("code")));
        name.setText(student.getString(student.getColumnIndex("name")));
        email.setText(student.getString(student.getColumnIndex("email")));
    }
}
