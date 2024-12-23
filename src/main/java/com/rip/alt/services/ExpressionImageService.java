package com.rip.alt.services;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rip.alt.exceptions.AuthorizationException;
import com.rip.alt.models.Expression;
import com.rip.alt.models.ExpressionImage;
import com.rip.alt.models.ExpressionImageRepository;
import com.rip.alt.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpressionImageService {

    @Autowired
    private ExpressionImageRepository repository;

    /**
     * Creates a new ExpressionImage based on the given expression,
     * and sets it as the expression's image.
     * @param expression the expression to generate an image for
     * @return the new ExpressionImage
     */
    public ExpressionImage attach(Expression expression) {
        var image = repository.findByExpressionId(expression.getId()).orElse(new ExpressionImage());
        image.setExpression(expression);
        image.setPublished(false);
        image.setContent(mathtex(expression));

        return repository.save(image);
    }

    /**
     * Checks if the current user is authorized to access the given expression.
     *
     * @param currentUser the user attempting to access the expression
     * @param expression the expression to check access for
     * @throws AuthorizationException if the current user is not the owner of the expression
     */
    public void authorize(User currentUser, Expression expression) {
        if (currentUser.getId() != expression.getUser().getId()) {
            throw new AuthorizationException();
        }
    }

    /**
     * Runs mathtex command to convert latex expression to gif image.
     * @param expression latex expression to convert
     * @return base64 encoded gif image
     * @throws RuntimeException if something went wrong
     */
    private String mathtex(Expression expression) {
        try {
            var temp_text = File.createTempFile("jpsln", "");
            var temp_image = File.createTempFile("jpsln", ".gif");

            BufferedWriter writer = new BufferedWriter(new FileWriter(temp_text));
            writer.write(expression.getLatex());
            writer.close();

            ProcessBuilder processBuilder = new ProcessBuilder("mathtex", "-f", temp_text.getAbsolutePath(), "-o",
                    temp_image.getAbsolutePath().replace(".gif", ""));
            Process process = processBuilder.start();
            process.waitFor();

            byte[] bytes = Files.readAllBytes(temp_image.toPath());

            temp_text.delete();
            temp_image.delete();

            return java.util.Base64.getEncoder().encodeToString(bytes);

        } catch (Exception e) {
            throw new RuntimeException(e); // ("Это никогда не случится :3");
        }
    }
}
