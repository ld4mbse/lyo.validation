/*-*****************************************************************************
 * Copyright (c) 2017 Yash Khatri.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 *
 * Contributors:
 *    Yash Khatri - initial API and implementation and/or initial documentation
 *******************************************************************************/

package org.eclipse.lyo.validation;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.text.ParseException;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileUtils;
import org.eclipse.lyo.oslc4j.core.exception.OslcCoreApplicationException;
import org.eclipse.lyo.validation.impl.ValidatorImpl;
import org.eclipse.lyo.validation.model.ResourceModel;
import org.eclipse.lyo.validation.model.ValidationResultModel;
import org.eclipse.lyo.validation.AResource;
import org.eclipse.lyo.validation.Validator;

/**
 * @author Yash Khatri
 * @version $version-stub$
 * @since 2.3.0
 */
public class ValidationExample {

    /**
     * Loads an example SHACL file and validates all focus nodes against all shapes.
     */
    public static void validateModelAgainstShaclModel() throws IllegalAccessException, InvocationTargetException,
            DatatypeConfigurationException, OslcCoreApplicationException {

        // Load the main data model
        Model dataModel = ModelFactory.createDefaultModel();
        dataModel.read(ValidationExample.class.getResourceAsStream("/aResource-Data.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        // Load the SHACL shape model
        Model shapeModel = ModelFactory.createDefaultModel();
        shapeModel.read(ValidationExample.class.getResourceAsStream("/aResource-Shape.ttl"), "urn:dummy",
                FileUtils.langTurtle);

        Validator validator = new ValidatorImpl();
        // Validate the data model against the SHACL shapes model. You can have shape
        // and data in same model.
        ValidationResultModel validationResult = validator.validate(dataModel, shapeModel);
        for (ResourceModel rm : validationResult.getInvalidResources()) {
            System.out.println(rm.getResult().show());
        }
    }

    /**
     * Creates an OSLC resource, and validates it against its class annotations
     * defining its SHACL Shape.
     */
    public static void validateOslcResourceWithShaclAnnotations()
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            OslcCoreApplicationException, URISyntaxException, ParseException, DatatypeConfigurationException {

        // Create an OSLC resource
        AResource aResource = new AResource();
        aResource.setAnIntegerProperty(new BigInteger("101"));
        aResource.setAStringProperty("ABC");

        // Validate the OSLC resource against the SHACL Shape annotations - as defined
        // in the class @shacl annotations.
        Validator validator = new ValidatorImpl();
        ValidationResultModel validationResult = validator.validate(aResource);
        for (ResourceModel rm : validationResult.getInvalidResources()) {
            System.out.println(rm.getResult().show());
        }
    }

    /**
     * Creates an OSLC resource, and validates it against its class annotations
     * defining its OSLC Shape.
     */
    public static void validateOslcResourceWithOslcAnnotations()
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            OslcCoreApplicationException, URISyntaxException, ParseException, DatatypeConfigurationException {

        // Create an OSLC resource
        AnOslcResource anOslcResource = new AnOslcResource();
        anOslcResource.setAnIntegerProperty(new BigInteger("101"));

        // Validate the OSLC resource against the OSLC Shape annotations - as defined in
        // the class @oslc annotations.
        Validator validator = new ValidatorImpl();
        ValidationResultModel validationResult = validator.validate(anOslcResource);
        for (ResourceModel rm : validationResult.getInvalidResources()) {
            System.out.println(rm.getResult().show());
        }
    }

    public static void main(String[] args) throws Exception {
        validateModelAgainstShaclModel();
        validateOslcResourceWithShaclAnnotations();
        validateOslcResourceWithOslcAnnotations();
    }
}
